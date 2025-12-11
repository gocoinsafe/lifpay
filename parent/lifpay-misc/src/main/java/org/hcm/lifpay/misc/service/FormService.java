package org.hcm.lifpay.misc.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.resp.CountryListResp;
import org.hcm.lifpay.misc.dto.req.FormInfoRequest;

import java.util.List;

public interface FormService {

    /**
     * 提交表单
     * @param request 表单请求
     * @return 提交结果
     */
    BaseResponse<String> submitForm(FormInfoRequest request);



    /**
     * 获取国家列表接口
     * @return 列表结果
     */
    BaseResponse<List<CountryListResp>> countryList();

}
