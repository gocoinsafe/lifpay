package org.hcm.lifpay.data.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.data.dto.req.TransactionListReq;
import org.hcm.lifpay.data.dto.req.TransactionSubmitReq;
import org.hcm.lifpay.data.dto.resp.TransactionListResp;
import org.hcm.lifpay.data.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/data/", consumes = "application/json")
@Slf4j
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    /**
     * 上报交易记录
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/transaction/submit")
    @Operation(summary = "上报交易记录")
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
    @Operation(summary = "查询交易记录列表")
    public @ResponseBody BaseResponse<CommonPage<TransactionListResp>> transactionList(@RequestBody TransactionListReq request) {
        log.info("transactionList request: " + JSON.toJSONString(request));
        BaseResponse<CommonPage<TransactionListResp>> response = transactionService.transactionList(request);
        log.info("transactionList response: " + JSON.toJSONString(response));
        return response;
    }



}
