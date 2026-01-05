package org.hcm.lifpay.misc.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.common.MiscResultEnum;
import org.hcm.lifpay.misc.common.SmsCodeStatusEnum;
import org.hcm.lifpay.misc.common.VerifyCodeTypeEnum;
import org.hcm.lifpay.misc.dao.entity.VerifyCodeDo;
import org.hcm.lifpay.misc.dao.repository.VerifyCodeRepository;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.providers.SMSProvider;
import org.hcm.lifpay.misc.service.MailService;
import org.hcm.lifpay.misc.service.PublicService;
import org.hcm.lifpay.util.CommonUtil;
import org.hcm.lifpay.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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
    private SMSProvider smsProvider;

    @Value("${message.sms.workload:00}")
    String workload;

    @Value("${misc.verifyCode.title}")
    String emailTitle;

    @Value("${misc.verifyCode.content}")
    String emailContent;




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
                // 调用短信发送（复用单例client，性能优化）
                SendSmsResponse sendSmsResponse = smsProvider.buildSMSRequest(req.getContact(), verifyCode);
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



    public Boolean smsPowVerify(String phone, Long timestamp, Integer random) {
        String verifyMsg = phone + timestamp + random;
        byte[] hash = HashUtil.sha256(verifyMsg.getBytes());
        String hashStr = Hex.toHexString(hash);
        return hashStr.startsWith(workload);
    }
}
