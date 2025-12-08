package org.hcm.lifpay.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dto.req.AddContactReq;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.resp.ContactListResp;

public interface ContactService {


    /**
     * 获取联系人列表
     * */
    BaseResponse<CommonPage<ContactListResp>> getContactList(ContactListReq req);


    /**
     * 新增联系人
     * */
    BaseResponse addContact(AddContactReq req);

    /**
     * 更新联系人信息
     * */
    BaseResponse contactUpdate(AddContactReq req);


    /**
     * 删除联系人信息
     * */
    BaseResponse contactDel(AddContactReq req);

}
