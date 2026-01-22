package org.hcm.lifpay.misc.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CountryListResp {


    @Schema(description = "id")
    Long id;

    @Schema(description = "中文名称")
    String cnName;

    @Schema(description = "英文名称")
    String enName;

    @Schema(description = "简称")
    String abbreviation;

    @Schema(description = "区域代码")
    String code;

    @Schema(description = "国家图标logo")
    String icon;




}
