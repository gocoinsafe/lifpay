package org.hcm.lifpay.user.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.req.TransactionSubmitReq;
import org.hcm.lifpay.user.dto.resp.ContactListResp;
import org.hcm.lifpay.user.dto.resp.TransactionListResp;
import org.hcm.lifpay.user.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user/", consumes = "application/json")
@Slf4j
public class UserTransactionController {



    @Autowired
    TransactionService transactionService;




    /**
     * 上报交易记录
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/transaction/submit")
    @ApiOperation(value = "上报交易记录", notes = "上报交易记录")
    public @ResponseBody BaseResponse<?> transactionSubmit(@RequestBody TransactionSubmitReq request) {
        log.info("transactionSubmit request: " + JSON.toJSONString(request));
        BaseResponse<?> response = transactionService.transactionSubmit(request);
        log.info("transactionSubmit response: " + JSON.toJSONString(response));
        return response;
    }



    /**
     * 查询交易记录列表
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/transaction/list")
    @ApiOperation(value = "查询交易记录列表", notes = "查询交易记录列表")
    public @ResponseBody BaseResponse<CommonPage<TransactionListResp>> transactionList(@RequestBody ContactListReq request) {
        log.info("transactionList request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<TransactionListResp>> response = transactionService.transactionList(request);
        log.info("transactionList response: " + JSON.toJSONString(response));
        return response;
    }






}
