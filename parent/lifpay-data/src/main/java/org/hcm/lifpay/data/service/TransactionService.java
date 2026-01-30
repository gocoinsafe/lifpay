package org.hcm.lifpay.data.service;

import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.data.dto.req.TransactionListReq;
import org.hcm.lifpay.data.dto.req.TransactionSubmitReq;
import org.hcm.lifpay.data.dto.resp.TransactionListResp;

public interface TransactionService {





    /**
     * app 上报交易列表
     *
     * */
    BaseResponse<?> transactionSubmit(TransactionSubmitReq request);



    /**
     * 获取交易列表
     * */
    BaseResponse<CommonPage<TransactionListResp>> transactionList(TransactionListReq request);



}
