package org.hcm.lifpay.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.constant.ContactStatus;
import org.hcm.lifpay.user.dao.entity.UserContactDo;
import org.hcm.lifpay.user.dao.repository.UserContactRepository;
import org.hcm.lifpay.user.dto.UserResultEnum;
import org.hcm.lifpay.user.dto.req.AddContactReq;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.resp.ContactListResp;
import org.hcm.lifpay.user.exception.LifpayException;
import org.hcm.lifpay.user.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.hcm.lifpay.common.CommonPage;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class ContactServiceImpl implements ContactService {


    @Autowired
    UserContactRepository userContactRepository;





    @Override
    public BaseResponse<CommonPage<ContactListResp>> getContactList(ContactListReq req) {
        log.info("getContactList.req:{}", JSON.toJSONString(req));

        BaseResponse<CommonPage<ContactListResp>> response = new BaseResponse<>();

        Page<UserContactDo> queryPage = new Page<>(req.getPageNo(),req.getPageSize());

        // 查询该用户下的联系人 并且状态是正常的
        LambdaQueryWrapper<UserContactDo> queryWrapper = new LambdaQueryWrapper<UserContactDo>()
                .eq(UserContactDo:: getUserId, req.getUserId())
                .eq(UserContactDo:: getStatus, ContactStatus.NORMAL.getType());

        queryWrapper.orderByDesc(UserContactDo:: getCreateTime);
        Page<UserContactDo> page = userContactRepository.selectPage(queryPage,queryWrapper);

        List<ContactListResp> contactListRespList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(page.getRecords())){
            // 抛出对外返回值内容
            for (UserContactDo  data :page.getRecords()){
                ContactListResp contactInfo = new ContactListResp();
                contactInfo.setId(data.getId());
                contactInfo.setContactName(data.getContactName());
                contactInfo.setAddress(data.getAddress());
                contactInfo.setEmail(data.getEmail());
                contactInfo.setNostr(data.getNostr());
                contactInfo.setNote(data.getNote());
                contactListRespList.add(contactInfo);
            }
        }

        response.setData(CommonPage.restPage(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), contactListRespList));
        return response;
    }


    @Override
    @Transactional
    public BaseResponse addContact(AddContactReq req) {
        log.info("addContact:{}",JSON.toJSONString(req));
        if (StringUtils.isEmpty(req.getContactName())){
            return BaseResponse.fail(UserResultEnum.CONTACT_USER_NAME_ERR.getCode(),UserResultEnum.CONTACT_USER_NAME_ERR.getMsg());
        }
        if (StringUtils.isEmpty(req.getAddress())){
            return BaseResponse.fail(UserResultEnum.CONTACT_ADDRESS_ERR.getCode(),UserResultEnum.CONTACT_ADDRESS_ERR.getMsg());
        }

        UserContactDo userContactDo = new UserContactDo();
        userContactDo.setContactName(req.getContactName());
        userContactDo.setAddress(req.getAddress());
        userContactDo.setEmail(req.getEmail());
        userContactDo.setNostr(req.getNostr());
        userContactDo.setNote(req.getNote());
        userContactDo.setStatus(ContactStatus.NORMAL.getType());
        userContactDo.setCreateTime(System.currentTimeMillis());
        userContactDo.setUpdateTime(System.currentTimeMillis());

        int result = userContactRepository.insert(userContactDo);
        if (result <= 0) {
            throw new LifpayException(UserResultEnum.ADD_CONTACT_USER_ERR.getCode(),
                    UserResultEnum.ADD_CONTACT_USER_ERR.getMsg());
        }

        return BaseResponse.success(UserResultEnum.SUCCESS);
    }


    @Override
    public BaseResponse contactUpdate(AddContactReq req) {
        log.info("contactUpdate:{}",JSON.toJSONString(req));
        if (StringUtils.isEmpty(req.getContactName())){
            return BaseResponse.fail(UserResultEnum.CONTACT_USER_NAME_ERR.getCode(),UserResultEnum.CONTACT_USER_NAME_ERR.getMsg());
        }
        if (StringUtils.isEmpty(req.getAddress())){
            return BaseResponse.fail(UserResultEnum.CONTACT_ADDRESS_ERR.getCode(),UserResultEnum.CONTACT_ADDRESS_ERR.getMsg());
        }
        if (null == req.getUserId()){
            return BaseResponse.fail(UserResultEnum.BAD_INPUT.getCode(),UserResultEnum.BAD_INPUT.getMsg());
        }
        // 查询该用户下的联系人信息
        LambdaQueryWrapper<UserContactDo> queryWrapper = new LambdaQueryWrapper<UserContactDo>()
                .eq(UserContactDo:: getId, req.getId())
                .eq(UserContactDo:: getUserId, req.getUserId())
                .eq(UserContactDo:: getStatus, ContactStatus.NORMAL.getType());

        List<UserContactDo> contactDoList = userContactRepository.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(contactDoList)){
            return BaseResponse.fail(UserResultEnum.DELETE_CONTACT_USER_ERR.getCode(),UserResultEnum.DELETE_CONTACT_USER_ERR.getMsg());
        }

        UserContactDo userContactDo = contactDoList.get(0);
        userContactDo.setContactName(req.getContactName());
        userContactDo.setAddress(req.getAddress());
        userContactDo.setNostr(req.getNostr());
        userContactDo.setEmail(req.getEmail());
        userContactDo.setNote(req.getNote());

        // 如果存在就更新
        int result = userContactRepository.updateById(userContactDo);
        if (result <= 0){
            throw new LifpayException(UserResultEnum.SYSTEM_INTERNAL_ERROR.getCode(),
                    UserResultEnum.SYSTEM_INTERNAL_ERROR.getMsg());
        }

        return BaseResponse.success(UserResultEnum.SUCCESS);
    }


    @Override
    public BaseResponse contactDel(AddContactReq req) {
        log.info("contactDel:{}", JSON.toJSONString(req));
        if (null == req.getUserId()){
            return BaseResponse.fail(UserResultEnum.BAD_INPUT.getCode(),UserResultEnum.BAD_INPUT.getMsg());
        }
        // 查询该用户下的联系人信息
        LambdaQueryWrapper<UserContactDo> queryWrapper = new LambdaQueryWrapper<UserContactDo>()
                .eq(UserContactDo:: getId, req.getId())
                .eq(UserContactDo:: getUserId, req.getUserId())
                .eq(UserContactDo:: getStatus, ContactStatus.NORMAL.getType());

        List<UserContactDo> contactDoList = userContactRepository.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(contactDoList)){
            return BaseResponse.fail(UserResultEnum.DELETE_CONTACT_USER_ERR.getCode(),UserResultEnum.DELETE_CONTACT_USER_ERR.getMsg());
        }
        // 如果存在就删除
        int result = userContactRepository.delete(queryWrapper);
        if (result <= 0){
            throw new LifpayException(UserResultEnum.DELETE_CONTACT_USER_EXCEPTION_ERR.getCode(),
                    UserResultEnum.DELETE_CONTACT_USER_EXCEPTION_ERR.getMsg());
        }
        return BaseResponse.success(UserResultEnum.DELETE_CONTACT_USER_SUCCESS);
    }
}
