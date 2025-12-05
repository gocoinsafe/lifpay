package org.hcm.lifpay.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.resp.ContactListResp;
import org.hcm.lifpay.user.service.ContactService;
import org.springframework.stereotype.Service;


@Service
public class ContactServiceImpl implements ContactService {


    @Override
    public BaseResponse<IPage<ContactListResp>> getContactList(BaseRequest req) {
        return null;
    }






}
