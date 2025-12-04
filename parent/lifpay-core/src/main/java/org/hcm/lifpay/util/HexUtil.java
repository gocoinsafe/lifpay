package org.hcm.lifpay.util;



import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.StringUtils;

public class HexUtil {

    private static final char[] HEX_CHAR = "0123456789abcdef".toCharArray();
    public static final String HEX_PREFIX = "0x";


    public static String toHexStringWithPrefix(byte[] input) {
        return HEX_PREFIX + Hex.toHexString(input);
    }

    public static String toHexStringNoPrefix(byte[] input) {
        return Hex.toHexString(input);
    }

    public static byte[] fromHexString(String input) {
        return Hex.decode(HexUtil.cleanHexPrefix(input));
    }


    public static String cleanHexPrefix(String input) {
        return containsHexPrefix(input) ? input.substring(2) : input;
    }

    public static String prependHexPrefix(String input) {
        return !containsHexPrefix(input) ? HEX_PREFIX + input : input;
    }

    public static boolean containsHexPrefix(String input) {
        return !StringUtils.isEmpty(input) && input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, false);
    }

    public static String toHexString(byte[] bytes, boolean prependPrefix) {
        if (null == bytes) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(bytes.length << 1);

            for (byte aByte : bytes) {
                sb.append(HEX_CHAR[(aByte & 240) >> 4]).append(HEX_CHAR[aByte & 15]);
            }

            String hexStr = sb.toString();
            return prependPrefix ? prependHexPrefix(hexStr) : hexStr;
        }
    }
}
