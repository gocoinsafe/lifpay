package org.hcm.lifpay.user.dto.resp;


import lombok.Data;

/**
 * @author 新哲
 */
@Data
public class QrCodeDto {

    private int platform;

    private Long userId;

    private int status;

    private String token;

    private String publicKey;

    private String refreshToken;

    private String appDeviceId;

    private String webDeviceId;

    public QrCodeDto() {
    }

    public QrCodeDto(String webDeviceId, int platform, int status, String publicKey) {
        this.platform = platform;
        this.status = status;
        this.webDeviceId = webDeviceId;
        this.publicKey = publicKey;
    }
}
