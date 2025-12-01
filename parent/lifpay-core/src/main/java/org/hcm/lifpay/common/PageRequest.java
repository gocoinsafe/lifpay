package org.hcm.lifpay.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest{
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", required = true)
    private int pageNumber;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量",required = true)
    private int pageSize;
}
