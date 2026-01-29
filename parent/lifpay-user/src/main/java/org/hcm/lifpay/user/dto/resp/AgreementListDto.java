package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AgreementListDto {

    @Schema(description = "用户协议列表信息")
    private List<AgreementQueryDto> list;
}
