package org.hcm.lifpay.user.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.AddContactReq;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.resp.ContactListResp;
import org.hcm.lifpay.user.dto.resp.LoginResponse;
import org.hcm.lifpay.user.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user/contact/", consumes = "application/json")
@Slf4j
public class ContactController {


    @Autowired
    ContactService contactService;



    /**
     * 获取当前用户联系人列表
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/list")
    @Operation(summary = "获取当前用户联系人列表")
    public @ResponseBody BaseResponse<CommonPage<ContactListResp>> getContactList(@RequestBody ContactListReq request) {
        log.info("getContactList request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<ContactListResp>> response = contactService.getContactList(request);
        log.info("getContactList response: " + JSON.toJSONString(response));
        return response;
    }



    /**
     * 添加联系人
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/add")
    @Operation(summary = "添加联系人")
    public @ResponseBody BaseResponse contactAdd(@RequestBody AddContactReq request) {
        log.info("contactAdd request: " + JSON.toJSONString(request));
        BaseResponse response = contactService.addContact(request);
        log.info("contactAdd response: " + JSON.toJSONString(response));
        return response;
    }

    /**
     * 修改联系人
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/update")
    @Operation(summary = "修改联系人信息")
    public @ResponseBody BaseResponse<IPage<ContactListResp>> contactUpdate(@RequestBody AddContactReq request) {
        log.info("contactUpdate request: " + JSON.toJSONString(request));
        BaseResponse response = contactService.contactUpdate(request);
        log.info("contactUpdate response: " + JSON.toJSONString(response));
        return response;
    }


    /**
     * 删除联系人
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/del")
    @Operation(summary = "删除联系人")
    public @ResponseBody BaseResponse<IPage<ContactListResp>> contactDel(@RequestBody AddContactReq request) {
        log.info("contactDel request: " + JSON.toJSONString(request));
        BaseResponse response = contactService.contactDel(request);
        log.info("contactDel response: " + JSON.toJSONString(response));
        return response;
    }


}
