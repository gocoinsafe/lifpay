package org.hcm.lifpay.dto;

import lombok.Data;
import org.hcm.lifpay.common.enums.AdminUserTypeEnum;

import java.util.List;

/**
 * 用户信息
 * @author xinzhe
 */
@Data
public class UserInfo {
    private Long userId;
    private String userName;
    private AdminUserTypeEnum userType;
    private List<String> userPermission;
}
