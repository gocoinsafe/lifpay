package org.hcm.lifpay.util;

import org.bouncycastle.crypto.digests.SHA256Digest;


public class HashUtil {

    public static byte[] sha256(byte[] msg) {
        SHA256Digest digest = new SHA256Digest();
        digest.update(msg, 0, msg.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    public static String getEncodeKey(String password) {
        byte[] hash = sha256(password.getBytes());
        return HexUtil.toHexString(hash).substring(0, 16);
    }
}
