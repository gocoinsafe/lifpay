package org.hcm.lifpay.exception;



import org.apache.commons.lang3.StringUtils;

/**
 * @author xinzhe
 */
public class NoPermissionException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NoPermissionException(String role)
    {
        super(role);
    }
    public NoPermissionException()
    {
        super("没有该访问权限");
    }
    public NoPermissionException(String[] roles)
    {
        super(StringUtils.join(roles, ","));
    }
}

