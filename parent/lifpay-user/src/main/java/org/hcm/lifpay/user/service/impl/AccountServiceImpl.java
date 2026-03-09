package org.hcm.lifpay.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.constant.Constant;
import org.hcm.lifpay.user.dao.entity.AuthAgreementDo;
import org.hcm.lifpay.user.dao.entity.ServiceTermsDo;
import org.hcm.lifpay.user.dao.repository.AuthAgreementRepository;
import org.hcm.lifpay.user.dao.repository.ServiceTermsRepository;
import org.hcm.lifpay.user.dto.UserResultEnum;
import org.hcm.lifpay.user.dto.req.AgreementAgreeReq;
import org.hcm.lifpay.user.dto.req.AgreementQueryReq;
import org.hcm.lifpay.user.dto.resp.AgreementListDto;
import org.hcm.lifpay.user.dto.resp.AgreementQueryDto;
import org.hcm.lifpay.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {

    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private ServiceTermsRepository serviceTermsRepository;

    @Autowired
    private AuthAgreementRepository authAgreementRepository;





    @Override
    public BaseResponse<AgreementListDto> agreementList(AgreementQueryReq req) {
        logger.info("agreementList req:{}", JSONObject.toJSONString(req));
        BaseResponse<AgreementListDto> response = new BaseResponse<>();

        LambdaQueryWrapper<ServiceTermsDo> queryWrapper = new LambdaQueryWrapper<ServiceTermsDo>()
                .eq(ServiceTermsDo::getStatus, Constant.ServiceTermsStatus.IN_USE)
                .orderByDesc(ServiceTermsDo::getCreateTime);

        // 根据时间排序 获取 正在使用中的协议
        List<ServiceTermsDo> list = serviceTermsRepository.selectList(queryWrapper);
        if (null == list){
            logger.info("Protocol does not exist");
            return response;
        }

        AgreementListDto agreementList = new AgreementListDto();

        List<AgreementQueryDto> queryDtoList = new ArrayList<>();
        //如果协议不为空 返回协议的列表
        for (ServiceTermsDo info:list){
            AgreementQueryDto queryDto = new AgreementQueryDto();

            queryDto.setAgreementVersion(info.getVersion());
            queryDto.setName(info.getName());
            queryDto.setType(info.getType().toString());
            queryDto.setUrl(info.getUrl());
            queryDto.setPath(info.getPath());
            queryDtoList.add(queryDto);
        }
        agreementList.setList(queryDtoList);

        response.setData(agreementList);
        response.setCode(UserResultEnum.SUCCESS.getCode());
        response.setMessage(UserResultEnum.SUCCESS.getMsg());
        return response;
    }

    @Override
    public BaseResponse<AgreementListDto> agreementQuery(AgreementQueryReq req) {
        logger.info("agreementQuery req:{}", JSONObject.toJSONString(req));
        BaseResponse<AgreementListDto> response = new BaseResponse<>();

        LambdaQueryWrapper<ServiceTermsDo> queryWrapper = new LambdaQueryWrapper<ServiceTermsDo>()
                .eq(ServiceTermsDo::getStatus, Constant.ServiceTermsStatus.IN_USE)
                .orderByDesc(ServiceTermsDo::getCreateTime);
        // 根据时间排序 获取 正在使用中的协议
        List<ServiceTermsDo> list = serviceTermsRepository.selectList(queryWrapper);
        if (null == list){
            logger.info("Protocol does not exist");
            return response;
        }

        AgreementListDto agreementList = new AgreementListDto();

        List<AgreementQueryDto> queryDtoList = new ArrayList<>();
        //如果协议不为空 查询该用户是否已同意该项条款
        for (ServiceTermsDo info:list){
            AgreementQueryDto queryDto = new AgreementQueryDto();

            LambdaQueryWrapper<AuthAgreementDo> queryAuthAgreementWrapper = new LambdaQueryWrapper<AuthAgreementDo>()
                    .eq(AuthAgreementDo::getUserId, req.getUserId())
                    .eq(AuthAgreementDo::getServiceTermsId, info.getId());


            // 根据用户id和 协议id 查询用户是否有通过操作
            AuthAgreementDo authInfo = authAgreementRepository.selectOne(queryAuthAgreementWrapper);
            if (null != authInfo && authInfo.getIsAgree().equals(Constant.IsAgree.AGREE)){
                queryDto.setAgree(true);
            }

            queryDto.setAgreementVersion(info.getVersion());
            queryDto.setName(info.getName());
            queryDto.setType(info.getType().toString());
            queryDto.setUrl(info.getUrl());
            queryDto.setPath(info.getPath());
            queryDtoList.add(queryDto);
        }
        agreementList.setList(queryDtoList);

        response.setData(agreementList);
        response.setCode(UserResultEnum.SUCCESS.getCode());
        response.setMessage(UserResultEnum.SUCCESS.getMsg());
        return response;
    }


    @Override
    public BaseResponse agreementAgree(AgreementAgreeReq req) {
        logger.info("agreementAgree req:{}", JSONObject.toJSONString(req));
        BaseResponse response = new BaseResponse();
        if (StringUtils.isEmpty(req.getType().toString()) && StringUtils.isEmpty(req.getAgreementVersion())){
            logger.info("agreementAgree type:{}, version:{}", req.getType(), req.getAgreementVersion());
            response.setCode(UserResultEnum.BAD_INPUT.getCode());
            response.setMessage(UserResultEnum.BAD_INPUT.getMsg());
            return response;
        }

        LambdaQueryWrapper<ServiceTermsDo> queryWrapper = new LambdaQueryWrapper<ServiceTermsDo>()
                .eq(ServiceTermsDo::getStatus, Constant.ServiceTermsStatus.IN_USE)
                .eq(ServiceTermsDo::getType, req.getType())
                .eq(ServiceTermsDo::getVersion, req.getAgreementVersion())
                .orderByDesc(ServiceTermsDo::getCreateTime);

        // 判断该协议类型是否存在
        List<ServiceTermsDo> serviceTermsDoList = serviceTermsRepository.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(serviceTermsDoList)){
            response.setCode(UserResultEnum.AGREEMENT_EXIT.getCode());
            response.setMessage(UserResultEnum.AGREEMENT_EXIT.getMsg());
            return response;
        }
        ServiceTermsDo serviceTermsDO = serviceTermsDoList.get(0);

        // 根据用户id和 协议id 查询用户是否有通过操作
        LambdaQueryWrapper<AuthAgreementDo> queryAuthAgreementWrapper = new LambdaQueryWrapper<AuthAgreementDo>()
                .eq(AuthAgreementDo::getUserId, req.getUserId())
                .eq(AuthAgreementDo::getServiceTermsId,serviceTermsDO.getId())
                .orderByDesc(AuthAgreementDo::getCreateTime);

        List<AuthAgreementDo> authInfoList = authAgreementRepository.selectList(queryAuthAgreementWrapper);
        if (CollectionUtil.isNotEmpty(authInfoList)){
            AuthAgreementDo authInfo = authInfoList.get(0);
            if (null != authInfo && authInfo.getIsAgree().equals(Constant.IsAgree.AGREE)){
                logger.info("Agreed, repeated agreed");
                response.setCode(UserResultEnum.AGREED_REPEATED_AGREED.getCode());
                response.setMessage(UserResultEnum.AGREED_REPEATED_AGREED.getMsg());
                return response;
            }
            // 补充逻辑 用户存在但同意状态为未同意 应该更改同意的状态
            authInfo.setIsAgree(Constant.IsAgree.AGREE);
            authInfo.setUpdateTime(System.currentTimeMillis());
            authAgreementRepository.updateById(authInfo);

            response.setCode(UserResultEnum.SUCCESS.getCode());
            response.setMessage(UserResultEnum.SUCCESS.getMsg());
            return response;
        }
        // 添加用户同意的信息到数据库
        AuthAgreementDo authAgreementDO = new AuthAgreementDo();
        authAgreementDO.setServiceTermsId(serviceTermsDO.getId());
        authAgreementDO.setUserId(req.getUserId());
        authAgreementDO.setDeviceId(req.getDeviceId());
        authAgreementDO.setIsAgree(Constant.IsAgree.AGREE);
        authAgreementDO.setCreateTime(System.currentTimeMillis());
        authAgreementRepository.insert(authAgreementDO);

        response.setCode(UserResultEnum.SUCCESS.getCode());
        response.setMessage(UserResultEnum.SUCCESS.getMsg());
        return response;
    }
}
