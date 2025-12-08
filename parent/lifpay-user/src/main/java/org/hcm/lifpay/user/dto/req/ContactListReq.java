package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class ContactListReq extends BaseRequest {


    @ApiModelProperty(value = "页码")
    Integer pageNo;

    @ApiModelProperty(value = "页数")
    Integer pageSize;



}
