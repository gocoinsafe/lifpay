package org.hcm.lifpay.misc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.DigitalResultEnum;
import org.hcm.lifpay.misc.common.ResultEnum;
import org.hcm.lifpay.misc.dao.dataobject.StoreFormEntity;
import org.hcm.lifpay.misc.dao.mapper.StoreFormMapper;
import org.hcm.lifpay.misc.service.FormService;
import org.hcm.lifpay.misc.vo.FormInfoRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;



@RefreshScope
@Service
@Slf4j
public class FormServiceImpl implements FormService {

    @Autowired
    private StoreFormMapper storeFormMapper;


    @Override
    public BaseResponse<String> submitForm(FormInfoRequest request) {
        log.info("开始提交表单，请求参数: {}", request);
        if (StringUtils.isNotEmpty(request.getHp()) || null != request.getTs()){
            // 如果honeypot 和 timestamp 有值就判定为机器人 直接返回
            return BaseResponse.success("表单提交成功");
        }
        
        try {
            // 参数校验
            if (StringUtils.isEmpty(request.getName())) {
                return BaseResponse.fail(ResultEnum.NAME_NOT_NULL_ERROR.getCode(), ResultEnum.NAME_NOT_NULL_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getName()) && request.getName().length() > 200) {
                return BaseResponse.fail(ResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), ResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            
            if (StringUtils.isEmpty(request.getEmail())) {
                return BaseResponse.fail(ResultEnum.EMAIL_INFORMATION_ERROR.getCode(), ResultEnum.EMAIL_INFORMATION_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getEmail()) && request.getEmail().length() > 200) {
                return BaseResponse.fail(ResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), ResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            if (StringUtils.isEmpty(request.getMessage())) {
                return BaseResponse.fail(ResultEnum.MESSAGE_NOT_NULL_ERROR.getCode(), ResultEnum.MESSAGE_NOT_NULL_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getMessage()) && request.getMessage().length() > 2000){
                return BaseResponse.fail(ResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), ResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            if (StringUtils.isEmpty(request.getCountry())){
                return BaseResponse.fail(ResultEnum.COUNTRY_NOT_NULL_ERROR.getCode(), ResultEnum.COUNTRY_NOT_NULL_ERROR.getDesc());
            }

            if (StringUtils.isNotEmpty(request.getCountry()) && request.getCountry().length() > 60){
                return BaseResponse.fail(ResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), ResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getPostCode()) && request.getPostCode().length() > 60){
                return BaseResponse.fail(ResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), ResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }


            
            // 创建实体对象
            StoreFormEntity entity = new StoreFormEntity();
            BeanUtils.copyProperties(request, entity);
            
            // 设置创建时间和更新时间
            long currentTime = System.currentTimeMillis();
            entity.setCreateTime(currentTime);
            entity.setUpdateTime(currentTime);
            
            // 设置默认值
            if (entity.getIsCustom() == null) {
                entity.setIsCustom(0); // 默认否
            }
            if (entity.getIdentity() == null) {
                entity.setIdentity(0); // 默认guest
            }
            
            // 插入数据库
            int result = storeFormMapper.insert(entity);
            
            if (result > 0) {
                log.info("表单提交成功，插入记录ID: {}", entity.getId());
                return BaseResponse.success("表单提交成功");
            } else {
                log.error("表单提交失败，数据库插入返回0");
                return BaseResponse.fail(DigitalResultEnum.FAIL.getCode(), "表单提交失败，请稍后重试");
            }
            
        } catch (Exception e) {
            log.error("表单提交异常", e);
            return BaseResponse.fail(DigitalResultEnum.FAIL.getCode(), "表单提交失败: " + e.getMessage());
        }
    }
}
