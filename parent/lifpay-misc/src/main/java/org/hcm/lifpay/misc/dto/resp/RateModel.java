package org.hcm.lifpay.misc.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RateModel {


    @Schema(description = "名称")
    private String name;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "价格")
    private String value;

    @Schema(description = "type: fiat:法币，crypto：代币")
    private String type;




}
