package org.hcm.lifpay.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.resp.ContactListResp;

public interface ContactService {



    BaseResponse<IPage<ContactListResp>> getContactList(BaseRequest req);



}
