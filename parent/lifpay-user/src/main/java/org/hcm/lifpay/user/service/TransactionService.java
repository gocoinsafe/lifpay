package org.hcm.lifpay.user.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.req.TransactionSubmitReq;
import org.hcm.lifpay.user.dto.resp.TransactionListResp;
import org.springframework.web.bind.annotation.RequestBody;

public interface TransactionService {





    /**
     * app 上报交易列表
     *
     * */
    BaseResponse<?> transactionSubmit(TransactionSubmitReq request);



    /**
     * 获取交易列表
     * */
    BaseResponse<CommonPage<TransactionListResp>> transactionList(ContactListReq request);



}
