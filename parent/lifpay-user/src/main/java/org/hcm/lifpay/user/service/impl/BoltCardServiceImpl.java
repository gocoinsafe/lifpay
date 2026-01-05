package org.hcm.lifpay.user.service.impl;

import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.BoltCardsInfoReq;
import org.hcm.lifpay.user.dto.req.CreateBoltCardReq;
import org.hcm.lifpay.user.dto.resp.CreateBoltCardResp;
import org.hcm.lifpay.user.service.BoltCardService;
import org.springframework.stereotype.Service;


@Service
public class BoltCardServiceImpl implements BoltCardService {


    @Override
    public BaseResponse<CreateBoltCardResp> createBoltCards(CreateBoltCardReq req) {
        return null;
    }


    @Override
    public BaseResponse<CreateBoltCardResp> getBoltCardsInfo(BoltCardsInfoReq req) {
        return null;
    }


    @Override
    public BaseResponse<CreateBoltCardResp> getBoltCardsBeyToken(BoltCardsInfoReq req) {
        return null;
    }


    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> queryBoltCards(BaseRequest req) {
        return null;
    }


    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTransactionQuery(BaseRequest req) {
        return null;
    }


    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsPinModify(BaseRequest req) {
        return null;
    }
}
