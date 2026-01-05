package org.hcm.lifpay.user.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.BoltCardsInfoReq;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.req.CreateBoltCardReq;
import org.hcm.lifpay.user.dto.resp.ContactListResp;
import org.hcm.lifpay.user.dto.resp.CreateBoltCardResp;
import org.hcm.lifpay.user.service.BoltCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user", consumes = "application/json")
@Slf4j
public class BoltCardController {


    @Autowired
    BoltCardService boltCardService;


    /**
     * 创建用户的bolt card
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/binding")
    @ApiOperation(value = "创建bolt card", notes = "create bolt card")
    public @ResponseBody BaseResponse<CreateBoltCardResp> createBoltCards(@RequestBody CreateBoltCardReq request) {
        log.info("bindingBoltCards request: " + JSON.toJSONString(request));
        BaseResponse<CreateBoltCardResp> response = boltCardService.createBoltCards(request);
        log.info("bindingBoltCards response: " + JSON.toJSONString(response));
        return response;
    }



    /**
     * 写卡流程（NFC 芯片初始化）
     * 用户拿到物理卡片后，需要使用 NFC 写卡器将数据写入芯片。有两种方式： 已登录用户直接获取数据
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/info/get")
    @ApiOperation(value = "获取bolt Card 信息", notes = "bolt card")
    public @ResponseBody BaseResponse<CreateBoltCardResp> getBoltCardsInfo(@RequestBody BoltCardsInfoReq request) {
        log.info("queryBoltCards request: " + JSON.toJSONString(request));
        BaseResponse<CreateBoltCardResp> response = boltCardService.getBoltCardsInfo(request);
        log.info("queryBoltCards response: " + JSON.toJSONString(response));
        return response;
    }


    /**
     * 写卡流程（NFC 芯片初始化）
     * 用户拿到物理卡片后，需要使用 NFC 写卡器将数据写入芯片。有两种方式： 未登录的 NFC 终端（通过 Token）
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/info/token")
    @ApiOperation(value = "查询用户的bolt card 列表", notes = "bolt card")
    public @ResponseBody BaseResponse<CreateBoltCardResp> getBoltCardsBeyToken(@RequestBody BoltCardsInfoReq request) {
        log.info("queryBoltCards request: " + JSON.toJSONString(request));
        BaseResponse<CreateBoltCardResp> response = boltCardService.getBoltCardsBeyToken(request);
        log.info("queryBoltCards response: " + JSON.toJSONString(response));
        return response;
    }




    /**
     * 5. 查询和管理流程
     * 查询用户的查询卡片列表
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/query")
    @ApiOperation(value = "查询用户的bolt card 列表", notes = "bolt card")
    public @ResponseBody BaseResponse<CommonPage<CreateBoltCardResp>> queryBoltCards(@RequestBody BaseRequest request) {
        log.info("queryBoltCards request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<CreateBoltCardResp>> response = boltCardService.queryBoltCards(request);
        log.info("queryBoltCards response: " + JSON.toJSONString(response));
        return response;
    }


    /**
     * 5.2 查询交易记录
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/transaction/query")
    @ApiOperation(value = "查询用户bolt card 交易记录", notes = "bolt card")
    public @ResponseBody BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTransactionQuery(@RequestBody BaseRequest request) {
        log.info("boltCardsTransactionQuery request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<CreateBoltCardResp>> response = boltCardService.boltCardsTransactionQuery(request);
        log.info("boltCardsTransactionQuery response: " + JSON.toJSONString(response));
        return response;
    }



    /**
     * 5.3 修改 PIN
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/pin/modify")
    @ApiOperation(value = "修改用户的pin", notes = "bolt card")
    public @ResponseBody BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsPinModify(@RequestBody BaseRequest request) {
        log.info("boltCardsPinModify request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<CreateBoltCardResp>> response = boltCardService.boltCardsPinModify(request);
        log.info("boltCardsPinModify response: " + JSON.toJSONString(response));
        return response;
    }


    /**
     * 5.4修改卡片类型/限额
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/type/modify")
    @ApiOperation(value = "修改卡片类型/限额", notes = "bolt card")
    public @ResponseBody BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTypeModify(@RequestBody BaseRequest request) {
        log.info("boltCardsTypeModify request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<CreateBoltCardResp>> response = boltCardService.boltCardsTypeModify(request);
        log.info("boltCardsTypeModify response: " + JSON.toJSONString(response));
        return response;
    }


    /**
     * 5.5 注销所有权
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/writeOff")
    @ApiOperation(value = "卡片注销所有权", notes = "bolt card")
    public @ResponseBody BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsWriteOff(@RequestBody BaseRequest request) {
        log.info("boltCardsTypeModify request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<CreateBoltCardResp>> response = boltCardService.boltCardsWriteOff(request);
        log.info("boltCardsTypeModify response: " + JSON.toJSONString(response));
        return response;
    }



    /**
     * 5.6 退款（管理员）
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/boltCard/refund/admin")
    @ApiOperation(value = "退款（管理员）", notes = "bolt card")
    public @ResponseBody BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsRefund(@RequestBody BaseRequest request) {
//        log.info("boltCardsRefund request: " + JSON.toJSONString(request));
//        BaseResponse<CommonPage<CreateBoltCardResp>> response = boltCardService.boltCardsRefund(request);
//        log.info("boltCardsRefund response: " + JSON.toJSONString(response));
        return null;
    }





}
