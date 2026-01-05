package org.hcm.lifpay.user.service;

import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.BoltCardsInfoReq;
import org.hcm.lifpay.user.dto.req.CreateBoltCardReq;
import org.hcm.lifpay.user.dto.resp.CreateBoltCardResp;

public interface BoltCardService {



    BaseResponse<CreateBoltCardResp> createBoltCards(CreateBoltCardReq req);



    BaseResponse<CreateBoltCardResp> getBoltCardsInfo(BoltCardsInfoReq req);


    BaseResponse<CreateBoltCardResp> getBoltCardsBeyToken(BoltCardsInfoReq req);


    BaseResponse<CommonPage<CreateBoltCardResp>> queryBoltCards(BaseRequest req);


    BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTransactionQuery(BaseRequest req);

    BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsPinModify(BaseRequest req);

    BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTypeModify(BaseRequest req);


    BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsWriteOff(BaseRequest req);
}
