package org.hcm.lifpay.misc.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.common.MiscResultEnum;
import org.hcm.lifpay.misc.common.SmsCodeStatusEnum;
import org.hcm.lifpay.misc.dao.entity.VerifyCodeDo;
import org.hcm.lifpay.misc.dao.repository.VerifyCodeRepository;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.exception.MiscException;
import org.hcm.lifpay.misc.req.InnerGetVerifyCodeReq;
import org.hcm.lifpay.misc.resp.GetVerifyCodeResp;
import org.hcm.lifpay.misc.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {


    @Autowired
    private VerifyCodeRepository verifyCodeRepository;



    @Override
    public BaseResponse<GetVerifyCodeResp> smsCodeVerify(InnerGetVerifyCodeReq request) {

        String logPrefix = "smsCodeVerify";
        BaseResponse<GetVerifyCodeResp> result = BaseResponse.success(new GetVerifyCodeResp());
        try {
//            SysParameter sysParameter = sysParameterDao.getByParamKey("UNIVERSAL_SMS_CODE");
//            if (sysParameter != null && StringUtils.isNotEmpty(sysParameter.getParamValue())) {
//                log.info("使用万能验证码={}", sysParameter.getParamValue());
//                if (sysParameter.getParamValue().equals(request.getCode())) {
//                    result.getData().setResult(true);
//                    return result;
//                }
//            }
            LambdaQueryWrapper<VerifyCodeDo> queryWrapper = new LambdaQueryWrapper<VerifyCodeDo>()
                    .eq(VerifyCodeDo:: getContact, request.getContact())
                    .eq(VerifyCodeDo:: getType, request.getType())
                    .orderByDesc(VerifyCodeDo:: getCreateTime)
                    .last("LIMIT 1");;

            VerifyCodeDo verifyCodeDo = verifyCodeRepository.selectOne(queryWrapper);

            // 没有记录
            if (verifyCodeDo == null) {
                throw new MiscException(MiscResultEnum.INVALID_SMS_CODE);
            }
            // 错误的验证码
            if (!verifyCodeDo.getVerifyCode().equals(request.getVerifyCode())) {
                throw new MiscException(MiscResultEnum.INVALID_SMS_CODE);
            }
            // 已使用
            if (SmsCodeStatusEnum.USED.getCode().equals(verifyCodeDo.getStatus())) {
                throw new MiscException(MiscResultEnum.EXPIRED_SMS_CODE);
            }
            // 3分钟内有效
            long systemTime = (new Date()).getTime();
            long createTime = verifyCodeDo.getCreateTime() + 3 * 60 * 1000;
            if (systemTime > createTime) {
                verifyCodeDo.setStatus(SmsCodeStatusEnum.EXPIRE.getCode());
                verifyCodeDo.setUpdateTime(System.currentTimeMillis());
                verifyCodeRepository.updateById(verifyCodeDo);
                throw new MiscException(MiscResultEnum.EXPIRED_SMS_CODE);
            }
            verifyCodeDo.setStatus(SmsCodeStatusEnum.USED.getCode());
            verifyCodeDo.setUpdateTime(System.currentTimeMillis());
            verifyCodeRepository.updateById(verifyCodeDo);
            // 返回
            result.getData().setResult(true);
        } catch (MiscException e) {
            result.setCode(e.getErrorCode());
            result.setMessage(e.getMessage());
            result.getData().setResult(false);
            log.error("{} fail: {} - {}", logPrefix, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            result.setCode(MiscResultEnum.FAILED.getCode());
            result.setMessage(MiscResultEnum.FAILED.getDesc());
            result.getData().setResult(false);
            log.error("{} fail", logPrefix, e);
        }
        return result;
    }
}
