package org.hcm.lifpay.misc.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountryListResp {


    @ApiModelProperty(value = "id")
    Long id;

    @ApiModelProperty(value = "中文名称")
    String cnName;

    @ApiModelProperty(value = "英文名称")
    String enName;

    @ApiModelProperty(value = "简称")
    String abbreviation;

    @ApiModelProperty(value = "区域代码")
    String code;

    @ApiModelProperty(value = "国家图标logo")
    String icon;




}
