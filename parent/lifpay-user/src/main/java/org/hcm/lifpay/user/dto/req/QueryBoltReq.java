package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class QueryBoltReq extends BaseRequest {


    @Schema(description = "页码")
    Integer pageNo;

    @Schema(description = "页数")
    Integer pageSize;

}
