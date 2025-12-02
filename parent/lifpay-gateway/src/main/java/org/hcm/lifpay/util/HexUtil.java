package org.hcm.lifpay.util;

import org.springframework.util.StringUtils;

import java.math.BigInteger;


public class HexUtil {

    private static final int[] DEC = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};
    private static final byte[] HEX = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final char[] HEX_CHAR = "0123456789abcdef".toCharArray();
    public static final String HEX_PREFIX = "0x";

    private static int getDec(int index) {
        try {
            return DEC[index - 48];
        } catch (ArrayIndexOutOfBoundsException var2) {
            return -1;
        }
    }

    public static byte getHex(int index) {
        return HEX[index];
    }

    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, false);
    }

    public static String toHexString(byte[] bytes, boolean prependPrefix) {
        if (null == bytes) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(bytes.length << 1);

            for (int i = 0; i < bytes.length; ++i) {
                sb.append(HEX_CHAR[(bytes[i] & 240) >> 4]).append(HEX_CHAR[bytes[i] & 15]);
            }
            String hexStr = sb.toString();
            return prependPrefix ? prependHexPrefix(hexStr) : hexStr;
        }
    }

    public static byte[] fromHexString(String input) {
        input = cleanHexPrefix(input);
        if (input == null) {
            return null;
        } else if ((input.length() & 1) == 1) {
            throw new IllegalArgumentException("hexUtils.fromHex.oddDigits");
        } else {
            char[] inputChars = input.toCharArray();
            byte[] result = new byte[input.length() >> 1];

            for (int i = 0; i < result.length; ++i) {
                int upperNibble = getDec(inputChars[2 * i]);
                int lowerNibble = getDec(inputChars[2 * i + 1]);
                if (upperNibble < 0 || lowerNibble < 0) {
                    throw new IllegalArgumentException("hexUtils.fromHex.nonHex");
                }

                result[i] = (byte) ((upperNibble << 4) + lowerNibble);
            }

            return result;
        }
    }

    public static String prependHexPrefix(String hexString) {
        return !containsHexPrefix(hexString) ? "0x" + hexString : hexString;
    }

    public static String cleanHexPrefix(String hexString) {
        return containsHexPrefix(hexString) ? hexString.substring(2) : hexString;
    }

    public static boolean containsHexPrefix(String hexString) {
        return !StringUtils.isEmpty(hexString) && hexString.length() > 1 && hexString.charAt(0) == '0' && hexString.charAt(1) == 'x';
    }
}
