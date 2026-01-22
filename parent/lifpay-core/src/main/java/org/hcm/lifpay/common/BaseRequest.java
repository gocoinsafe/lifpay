package org.hcm.lifpay.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
public class BaseRequest {
    private String deviceId;
    private String os;
    private String version;
    @Schema(hidden = true)
    private Long userId;
    private Long timestamp;
}

