package org.hcm.lifpay.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageRequest{
    /**
     * 页码
     */
    @Schema(description = "页码", required = true)
    private int pageNumber;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量",required = true)
    private int pageSize;
}
