package org.hcm.lifpay.misc.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.common.MiscResultEnum;
import org.hcm.lifpay.misc.common.SmsCodeStatusEnum;
import org.hcm.lifpay.misc.common.VerifyCodeTypeEnum;
import org.hcm.lifpay.misc.constant.MiscConstant;
import org.hcm.lifpay.misc.dao.entity.VerifyCodeDo;
import org.hcm.lifpay.misc.dao.repository.VerifyCodeRepository;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.dto.resp.CoinGeckoRateResp;
import org.hcm.lifpay.misc.dto.resp.ExchangeRateModel;
import org.hcm.lifpay.misc.dto.resp.RateModel;
import org.hcm.lifpay.misc.service.MailService;
import org.hcm.lifpay.misc.service.PublicService;
import org.hcm.lifpay.redis.RedisDS;
import org.hcm.lifpay.util.CommonUtil;
import org.hcm.lifpay.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.common.Credential;
//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import javax.annotation.Resource;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
@RefreshScope
@Service
public class PublicServiceImpl extends ServiceImpl<VerifyCodeRepository, VerifyCodeDo> implements PublicService {

    private final static Logger logger = LoggerFactory.getLogger(PublicServiceImpl.class);


    @Autowired
    private VerifyCodeRepository verifyCodeRepository;

    @Autowired
    private MailService mailService;

    @Resource
//    private SMSProvider smsProvider;

    // 核心修复：去掉@Value默认值冒号后的空格
    @Value("${misc.sms.secretId:}") // 无默认值，强制从Nacos读取（便于发现配置缺失）
    private String secretId;

    @Value("${misc.sms.secretKey:}")
    private String secretKey;

    @Value("${misc.sms.sdkAppId:2400001156}") // 去掉空格
    private String sdkAppId;

    @Value("${misc.sms.signName:LifPay}") // 去掉空格
    private String signName;

    @Value("${misc.sms.templateId:2929134}") // 去掉空格
    private String templateId;

    // ========== 性能核心：单例复用SmsClient（只初始化一次） ==========
    private SmsClient smsClient;

    private final ReentrantLock lock = new ReentrantLock();


    @Value("${message.sms.workload:00}")
    String workload;

    @Value("${misc.verifyCode.title}")
    String emailTitle;

    @Value("${misc.verifyCode.content}")
    String emailContent;

    @Value("${misc.coinGecko.url: https://api.coingecko.com/api/v3/exchange_rates}")
    String coinGeckoUrl;

    // 汇率缓存过期时间 15分钟
    Integer EXCHANGE_RATE_TIME= 60* 5;

    @Autowired
    protected RedisDS redisDS;




    @Override
    public BaseResponse<String> getVerifyCode(GetVerifyCodeReq req) {
        logger.info("PublicServiceImpl.getVerifyCode.req:{}", JSON.toJSONString(req));
        // 1. 参数校验（保留你的原有逻辑，优化提示）
        if (StringUtils.isEmpty(req.getContact())) {
            return BaseResponse.fail(MiscResultEnum.CONTACT_NOT_NULL_ERROR.getCode(), MiscResultEnum.CONTACT_NOT_NULL_ERROR.getDesc());
        }
        if (null == req.getType()) {
            return BaseResponse.fail(MiscResultEnum.CONTACT_TYPE_NOT_NULL_ERROR.getCode(), MiscResultEnum.CONTACT_TYPE_NOT_NULL_ERROR.getDesc());
        }
        if (!VerifyCodeTypeEnum.EMAIL.getType().equals(req.getType()) && !VerifyCodeTypeEnum.PHONE.getType().equals(req.getType())) {
            return BaseResponse.fail(MiscResultEnum.PARAM_ERROR.getCode(), MiscResultEnum.PARAM_ERROR.getDesc());
        }

        // 验证工作量证明随机数
        boolean powVerify = smsPowVerify(req.getContact(), req.getTimestamp(), req.getRandom());
//        if (!powVerify) {
//            return BaseResponse.fail(MiscResultEnum.INVALID_RANDOM.getCode(),MiscResultEnum.INVALID_RANDOM.getDesc());
//        }


        BaseResponse<String> response = new BaseResponse<>();
        // 生成6位数验证码（确保CommonUtil用的是SecureRandom）
        String verifyCode = CommonUtil.getRandomInteger(6);

        // 3. 组装保存对象（保留你的原有逻辑）
        VerifyCodeDo verifyCodeDo = new VerifyCodeDo();
        // 2. 手机号短信逻辑
        if (VerifyCodeTypeEnum.PHONE.getType().equals(req.getType())) {
            try {
                // 拼接完整手机号（E.164格式）
                String fullPhone = req.getArea() + req.getContact();
                // 校验手机号格式
                if (StringUtils.isEmpty(fullPhone) || !fullPhone.startsWith("+")) {
                    throw new IllegalArgumentException("手机号必须为E.164格式（+国家码+手机号），当前值：" + maskPhone(fullPhone));
                }
                // 校验核心配置
                if (StringUtils.isEmpty(secretId) || StringUtils.isEmpty(secretKey)) {
                    throw new RuntimeException("短信配置未初始化：secretId/secretKey为空");
                }

                // 构建短信请求并发送
                SendSmsResponse sendSmsResponse = sendSms(fullPhone, verifyCode);
                verifyCodeDo.setType(VerifyCodeTypeEnum.PHONE.getType());
                verifyCodeDo.setArea(req.getArea());

            } catch (IllegalArgumentException e) {
                logger.error("参数错误：{}", e.getMessage());
                return BaseResponse.fail(MiscResultEnum.PARAM_ERROR.getCode(), e.getMessage());
            } catch (TencentCloudSDKException e) {
                logger.error("短信发送失败：{}", e.getMessage());
                return BaseResponse.fail(MiscResultEnum.SMS_SEND_ERROR.getCode(), MiscResultEnum.SMS_SEND_ERROR.getDesc());
            } catch (Exception e) {
                logger.error("验证码发送异常", e);
                return BaseResponse.fail(MiscResultEnum.SYSTEM_BUSY.getCode(), "系统异常，请稍后重试");
            }
        }else if (VerifyCodeTypeEnum.EMAIL.getType().equals(req.getType())){
            verifyCodeDo.setType(VerifyCodeTypeEnum.EMAIL.getType());
            // 发送邮件 验证码
            mailService.sendSimpleMail(req.getContact(), emailTitle, emailContent + ":\n\n" + verifyCode);
        }else {
            response.setMessage("暂不支持邮箱、手机号以外的类型！");
        }

        verifyCodeDo.setContact(req.getContact());
        verifyCodeDo.setVerifyCode(verifyCode);
        verifyCodeDo.setStatus(SmsCodeStatusEnum.NOT_USED.getCode());
        verifyCodeDo.setCreateTime(System.currentTimeMillis());
        verifyCodeDo.setUpdateTime(System.currentTimeMillis());
        // 4. 保存验证码
        int result = verifyCodeRepository.insert(verifyCodeDo);
        if (result <= 0) {
            logger.warn("保存验证码失败，手机号：{}", req.getContact());
        }

        return BaseResponse.success("验证码发送成功");
    }


    // ========== 新增：短信发送核心方法（替代原SMSProvider.buildSMSRequest） ==========
    private SendSmsResponse sendSms(String fullPhone, String verifyCode) throws TencentCloudSDKException {
        // 构建短信请求参数
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppId(sdkAppId);
        req.setSignName(signName);
        req.setTemplateId(templateId);
        req.setSenderId("");
        req.setSessionContext("xxx");
        req.setExtendCode("");
        req.setPhoneNumberSet(new String[]{fullPhone});
        req.setTemplateParamSet(new String[]{MiscConstant.LIFPAY, verifyCode});

        // 发送请求（复用SmsClient）
        SendSmsResponse res = getSmsClient().SendSms(req);
        logger.info("短信发送成功，手机号：{}，RequestId：{}", maskPhone(fullPhone), res.getRequestId());
        return res;
    }

    // ========== 新增：获取SmsClient（懒加载+线程安全） ==========
    private SmsClient getSmsClient() {
        if (smsClient == null || !isConfigValid()) {
            lock.lock();
            try {
                if (smsClient == null || !isConfigValid()) {
                    logger.info("初始化/重建SmsClient（首次加载/配置变更）");
                    smsClient = createSmsClient();
                }
            } finally {
                lock.unlock();
            }
        }
        return smsClient;
    }

    // ========== 新增：创建SmsClient ==========
    private SmsClient createSmsClient() {
        try {
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setConnTimeout(60);
            httpProfile.setWriteTimeout(10);
            httpProfile.setReadTimeout(10);
            httpProfile.setEndpoint(MiscConstant.ENDPOINT);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(MiscConstant.SIGN_METHOD);
            clientProfile.setHttpProfile(httpProfile);

            return new SmsClient(cred, MiscConstant.SMS_REGION, clientProfile);
        } catch (Exception e) {
            logger.error("创建SmsClient失败", e);
            throw new RuntimeException("短信客户端初始化失败", e);
        }
    }

    // ========== 新增：辅助方法 ==========
    // 校验配置是否有效
    private boolean isConfigValid() {
        return !StringUtils.isEmpty(secretId) && !StringUtils.isEmpty(secretKey);
    }


    public Boolean smsPowVerify(String phone, Long timestamp, Integer random) {
        String verifyMsg = phone + timestamp + random;
        byte[] hash = HashUtil.sha256(verifyMsg.getBytes());
        String hashStr = Hex.toHexString(hash);
        return hashStr.startsWith(workload);
    }


    @Override
    public BaseResponse<ExchangeRateModel> getExchangeRate(BaseRequest request) {
        logger.info("getExchangeRate.req:{}",JSON.toJSONString(request));

        BaseResponse<ExchangeRateModel> response = new BaseResponse<>();
        // CoinGecko汇率接口地址
        final String COIN_GECKO_RATE_URL = coinGeckoUrl;

        // 初始化RestTemplate（如果已全局注入，可直接使用注入的实例）
        RestTemplate restTemplate = new RestTemplate();
        String cacheData = redisDS.getStr(MiscConstant.MISC_EXCHANGE_RATE);
        if (!StringUtils.isEmpty(cacheData)) {
            ExchangeRateModel model = JSON.parseObject(cacheData, ExchangeRateModel.class);
            response.setData(model);
            return response;
        }

        try {
            // 1. 调用CoinGecko接口，获取原始响应
            logger.info("开始调用CoinGecko汇率接口，URL：{}", COIN_GECKO_RATE_URL);
            ResponseEntity<String> apiResponse = restTemplate.getForEntity(COIN_GECKO_RATE_URL, String.class);

            // 2. 校验接口响应状态
            if (!HttpStatus.OK.equals(apiResponse.getStatusCode())) {
                logger.error("CoinGecko接口调用失败，响应状态码：{}", apiResponse.getStatusCodeValue());
                response.setCode(MiscResultEnum.EXCHANGE_RATE_FAILED.getCode());
                response.setMessage(MiscResultEnum.EXCHANGE_RATE_FAILED.getChMsg());
                return response;
            }

            // 3. 解析JSON响应为实体类
            String responseBody = apiResponse.getBody();
            logger.info("CoinGecko接口返回数据：{}", responseBody);
            CoinGeckoRateResp rateResponse = JSON.parseObject(responseBody, CoinGeckoRateResp.class);

            // 4. 提取btc、cny、usd三个币种的汇率数据
            ExchangeRateModel rateModel = new ExchangeRateModel();
            if (rateResponse != null && rateResponse.getRates() != null) {
                rateModel.setBtc(rateResponse.getRates().get(MiscConstant.MISC_EXCHANGE_RATE_BTC));
                rateModel.setCny(rateResponse.getRates().get(MiscConstant.MISC_EXCHANGE_RATE_CNY));
                rateModel.setUsd(rateResponse.getRates().get(MiscConstant.MISC_EXCHANGE_RATE_USD));
            }

            // 5. 校验核心数据是否存在
            if (rateModel.getBtc() == null || rateModel.getCny() == null || rateModel.getUsd() == null) {
                logger.error("汇率数据解析异常，缺失btc/cny/usd数据");
                response.setCode(MiscResultEnum.EXCHANGE_RATE_DATA_PARSING_FAILED.getCode());
                response.setMessage(MiscResultEnum.EXCHANGE_RATE_DATA_PARSING_FAILED.getChMsg());
                return response;
            }

            // 6. 封装成功响应
            response.setCode(MiscResultEnum.SUCCESS.getCode());
            response.setMessage(MiscResultEnum.SUCCESS.getChMsg());
            response.setData(rateModel);
            // 设置数据缓存时间 15分钟
            String jsonStr = JSON.toJSONString(rateModel);
            redisDS.setex(MiscConstant.MISC_EXCHANGE_RATE,jsonStr, EXCHANGE_RATE_TIME);

        } catch (RestClientException e) {
            // 处理HTTP请求异常（网络问题、接口不可达等）
            logger.error("调用CoinGecko汇率接口时发生网络异常", e);
            response.setCode(MiscResultEnum.EXCHANGE_RATE_FAILED.getCode());
            response.setMessage(MiscResultEnum.EXCHANGE_RATE_FAILED.getDesc());
        } catch (Exception e) {
            // 处理其他异常（JSON解析、数据转换等）
            logger.error("解析汇率数据时发生异常", e);
            response.setCode(MiscResultEnum.EXCHANGE_RATE_DATA_PARSING_FAILED.getCode());
            response.setMessage(MiscResultEnum.EXCHANGE_RATE_DATA_PARSING_FAILED.getDesc());
        }

        return response;
    }



    // ========== 工具方法：手机号脱敏（日志合规） ==========
    private String maskPhone(String phone) {
        if (StringUtils.isEmpty(phone) || phone.length() < 8) {
            return phone;
        }
        // 脱敏规则：+8615970297950 → +86159****7950
        return phone.substring(0, 5) + "****" + phone.substring(9);
    }
}
