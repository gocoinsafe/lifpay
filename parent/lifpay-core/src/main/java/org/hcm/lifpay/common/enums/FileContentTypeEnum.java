package org.hcm.lifpay.common.enums;


/**
 * 文件上传类型
 *
 * @author xinzhe
 */
public enum FileContentTypeEnum {

    /**
     * PNG
     */
    PNG(0, "image/png"),
    /**
     * JPG
     */
    JPG(1, "image/jpg"),
    /**
     * JPEG
     */
    JPEG(2, "image/jpeg"),
    /**
     * BMP
     */
    BMP(3, "image/bmp"),
    ;

    private final Integer code;
    private final String desc;

    FileContentTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FileContentTypeEnum getEnum(Integer code) {
        if (null == code) {
            return null;
        }
        for (FileContentTypeEnum item : FileContentTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }








}
