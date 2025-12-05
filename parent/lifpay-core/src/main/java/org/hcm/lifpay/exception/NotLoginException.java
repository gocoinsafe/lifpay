package org.hcm.lifpay.exception;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xinzhe
 */
public class NotLoginException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotLoginException(String role)
    {
        super(role);
    }
    public NotLoginException()
    {
        super("未登录");
    }
    public NotLoginException(String[] roles)
    {
        super(StringUtils.join(roles, ","));
    }
}

