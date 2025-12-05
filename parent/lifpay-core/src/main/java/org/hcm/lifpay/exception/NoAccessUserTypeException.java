package org.hcm.lifpay.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xinzhe
 */
public class NoAccessUserTypeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NoAccessUserTypeException(String role)
    {
        super(role);
    }
    public NoAccessUserTypeException()
    {
        super("没有该访问权限");
    }
    public NoAccessUserTypeException(String[] roles)
    {
        super(StringUtils.join(roles, ","));
    }
}
