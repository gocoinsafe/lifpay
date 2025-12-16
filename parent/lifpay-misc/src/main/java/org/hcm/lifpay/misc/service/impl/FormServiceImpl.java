package org.hcm.lifpay.misc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.DigitalResultEnum;
import org.hcm.lifpay.misc.common.MiscResultEnum;
import org.hcm.lifpay.misc.constant.CountryStatus;
import org.hcm.lifpay.misc.dao.entity.CountryListDo;
import org.hcm.lifpay.misc.dao.entity.StoreFormDo;
import org.hcm.lifpay.misc.dao.repository.CountryListRepository;
import org.hcm.lifpay.misc.dao.repository.StoreFormRepository;
import org.hcm.lifpay.misc.dto.resp.CountryListResp;
import org.hcm.lifpay.misc.service.FormService;
import org.hcm.lifpay.misc.dto.req.FormInfoRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


@RefreshScope
@Service
@Slf4j
public class FormServiceImpl extends ServiceImpl<StoreFormRepository,StoreFormDo> implements FormService {

    @Autowired
    private StoreFormRepository storeFormMapper;

    @Autowired
    private CountryListRepository countryListRepository;




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
                return BaseResponse.fail(MiscResultEnum.NAME_NOT_NULL_ERROR.getCode(), MiscResultEnum.NAME_NOT_NULL_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getName()) && request.getName().length() > 200) {
                return BaseResponse.fail(MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            
            if (StringUtils.isEmpty(request.getEmail())) {
                return BaseResponse.fail(MiscResultEnum.EMAIL_INFORMATION_ERROR.getCode(), MiscResultEnum.EMAIL_INFORMATION_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getEmail()) && request.getEmail().length() > 200) {
                return BaseResponse.fail(MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            if (StringUtils.isEmpty(request.getMessage())) {
                return BaseResponse.fail(MiscResultEnum.MESSAGE_NOT_NULL_ERROR.getCode(), MiscResultEnum.MESSAGE_NOT_NULL_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getMessage()) && request.getMessage().length() > 2000){
                return BaseResponse.fail(MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            if (StringUtils.isEmpty(request.getCountry())){
                return BaseResponse.fail(MiscResultEnum.COUNTRY_NOT_NULL_ERROR.getCode(), MiscResultEnum.COUNTRY_NOT_NULL_ERROR.getDesc());
            }

            if (StringUtils.isNotEmpty(request.getCountry()) && request.getCountry().length() > 60){
                return BaseResponse.fail(MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }
            if (StringUtils.isNotEmpty(request.getPostCode()) && request.getPostCode().length() > 60){
                return BaseResponse.fail(MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getCode(), MiscResultEnum.ILLEGAL_CHARACTERS_ERROR.getDesc());
            }


            
            // 创建实体对象
            StoreFormDo entity = new StoreFormDo();
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


    @Override
    public BaseResponse<List<CountryListResp>> countryList() {
        log.info("countryList");
        BaseResponse<List<CountryListResp>> response = new BaseResponse<>();

        LambdaQueryWrapper<CountryListDo> queryWrapper = new LambdaQueryWrapper<CountryListDo>()
                .eq(CountryListDo:: getStatus, CountryStatus.NORMAL);

        List<CountryListDo> countryListDoList = countryListRepository.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(countryListDoList)){
            log.info("国家列表信息为空！数据异常！:{}", countryListDoList.size());
            return null;
        }
        List<CountryListResp> dataList = new ArrayList<>();
        for (CountryListDo info :countryListDoList){
            CountryListResp data = new CountryListResp();
            data.setId(info.getId());
            data.setCnName(info.getCnName());
            data.setEnName(info.getEnName());
            data.setCode(info.getCode());
            data.setAbbreviation(info.getAbbreviation());
            dataList.add(data);

        }
        response.setData(dataList);
        return response;
    }
}
