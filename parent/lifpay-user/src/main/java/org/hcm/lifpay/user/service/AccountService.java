package org.hcm.lifpay.user.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.AgreementAgreeReq;
import org.hcm.lifpay.user.dto.req.AgreementQueryReq;
import org.hcm.lifpay.user.dto.resp.AgreementListDto;

public interface AccountService {

    /**
     * 用户协议查询 协议列表
     * */
    BaseResponse<AgreementListDto> agreementList(AgreementQueryReq req);

    /**
     * 用户协议查询 协议查询
     * */
    BaseResponse<AgreementListDto> agreementQuery(AgreementQueryReq req);

    /**
     * 用户同意协议
     * */
    public BaseResponse agreementAgree(AgreementAgreeReq req);
}
