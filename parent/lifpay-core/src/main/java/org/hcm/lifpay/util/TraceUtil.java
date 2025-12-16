package org.hcm.lifpay.util;

import java.util.UUID;

/**
 * @author xinzhe
 */
public class TraceUtil {
    public static String genTraceId(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}

