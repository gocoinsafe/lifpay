package org.hcm.lifpay.misc.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.vo.FormInfoRequest;

public interface FormService {

    /**
     * 提交表单
     * @param request 表单请求
     * @return 提交结果
     */
    BaseResponse<String> submitForm(FormInfoRequest request);

}
