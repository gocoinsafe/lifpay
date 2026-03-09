package org.hcm.lifpay.misc.providers;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.misc.constant.MiscConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
// 导入对应SMS模块的client
import com.tencentcloudapi.sms.v20210111.SmsClient;
// 导入要请求接口对应的request response类
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@RefreshScope
@Slf4j
@Component
public class SMSProvider {

    // ========== 仅保留核心配置的@Value注入（修复secretKey的key错误） ==========
    @Value("${misc.sms.secretId}")
    private String secretId;
    @Value("${misc.sms.secretKey}") // 修复：原用了secretId的key
    private String secretKey;
    @Value("${misc.sms.sdkAppId: 2400001156}")
    private String sdkAppId;
    @Value("${misc.sms.signName: LifPay}")
    private String signName;
    @Value("${misc.sms.templateId: 2929134}")
    private String templateId;

    // ========== 性能核心：单例复用SmsClient（只初始化一次） ==========
    private SmsClient smsClient;

    // Spring初始化时创建SmsClient，后续所有请求复用
    @PostConstruct
    public void initSmsClient() {
        try {
            // 1. 认证信息（只初始化一次）
            Credential cred = new Credential(secretId, secretKey);
            // 2. HTTP配置（用你已有的常量/硬编码，保留你的超时配置）
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setConnTimeout(60); // 你原有的超时配置，如需调整可改为常量
            httpProfile.setWriteTimeout(10);
            httpProfile.setReadTimeout(10);
            httpProfile.setEndpoint(MiscConstant.ENDPOINT); // 用你已有的常量

            // 3. 客户端配置（用你已有的常量）
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(MiscConstant.SIGN_METHOD);
            clientProfile.setHttpProfile(httpProfile);

            // 4. 创建SmsClient（单例复用，地域用你已有的常量）
            smsClient = new SmsClient(cred, MiscConstant.SMS_REGION, clientProfile);
            log.info("腾讯云SmsClient初始化成功");
        } catch (Exception e) {
            log.error("腾讯云SmsClient初始化失败", e);
            throw new RuntimeException("短信客户端初始化失败", e); // 启动失败，避免运行时异常
        }
    }

    /**
     * 发送短信验证码（优化后：复用单例client，完善校验/异常）
     * @param phoneNumbers 完整手机号（E.164格式：+国家码+手机号）
     * @param verifyCode   6位验证码
     * @return SendSmsResponse
     * @throws TencentCloudSDKException 抛出具体异常，上层统一处理
     */
    public SendSmsResponse buildSMSRequest(String phoneNumbers, String verifyCode) throws TencentCloudSDKException {
        // 1. 基础参数校验（避免无效请求）
        if (StringUtils.isEmpty(phoneNumbers) || !phoneNumbers.startsWith("+")) {
            throw new IllegalArgumentException("手机号必须为E.164格式，当前值：" + maskPhone(phoneNumbers));
        }

        try {
            // 2. 构建请求参数（移除冗余赋值，直接用成员变量）
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId(sdkAppId);
            req.setSignName(signName);
            req.setTemplateId(templateId);
            // 非核心配置用你原有的硬编码/常量
            req.setSenderId("");
            req.setSessionContext("xxx");
            req.setExtendCode("");
            req.setPhoneNumberSet(new String[]{phoneNumbers});
            req.setTemplateParamSet(new String[]{MiscConstant.LIFPAY, verifyCode}); // 用你已有的常量

            // 3. 发送请求（复用单例SmsClient，性能核心优化）
            SendSmsResponse res = smsClient.SendSms(req);
            log.info("短信发送成功，手机号：{}，RequestId：{}", maskPhone(phoneNumbers), res.getRequestId());
            return res;
        } catch (TencentCloudSDKException e) {
            log.error("短信发送失败，手机号：{}，错误码：{}，错误信息：{}", maskPhone(phoneNumbers), e.getErrorCode(), e.getMessage());
            throw e; // 抛出异常，上层处理，不返回null
        }
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
