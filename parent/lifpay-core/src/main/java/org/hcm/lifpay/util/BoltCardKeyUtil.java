package org.hcm.lifpay.util;



import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.hcm.lifpay.dto.ParsedSun;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;


public class BoltCardKeyUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    // NTAG424 SV2 constant
    private static final byte[] SV2 = Hex.decode("3CC300010080");

    /* =========================================================
     * Key generation (k0 k1 k2)
     * ========================================================= */
    public static String generateKeyHex() {
        byte[] key = new byte[16];
        RANDOM.nextBytes(key);
        return Hex.toHexString(key);
    }

    /* =========================================================
     * SUN decrypt (AES-ECB)
     * ========================================================= */
    public static byte[] decryptSUN(String pHex, String k1Hex) {
        try {
            byte[] encrypted = Hex.decode(pHex);
            byte[] key = Hex.decode(k1Hex);

            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("SUN decrypt failed", e);
        }
    }

    public static ParsedSun parseUIDAndCounter(String pHex, String k1Hex) {
        byte[] decrypted = decryptSUN(pHex, k1Hex);

        byte[] uid = new byte[7];
        byte[] counter = new byte[3];

        // decrypted[0] is RFU
        System.arraycopy(decrypted, 1, uid, 0, 7);
        System.arraycopy(decrypted, 8, counter, 0, 3);

        return new ParsedSun(
                Hex.toHexString(uid),
                Hex.toHexString(counter),
                bytesToInt(counter)
        );
    }

    /* =========================================================
     * CMAC (same as node-aes-cmac)
     * ========================================================= */
    public static String generateCmac(
            String uidHex,
            String counterHex,
            String k2Hex
    ) {

        byte[] uid = Hex.decode(uidHex);
        byte[] counter = Hex.decode(counterHex);

        byte[] message = new byte[SV2.length + uid.length + counter.length];
        System.arraycopy(SV2, 0, message, 0, SV2.length);
        System.arraycopy(uid, 0, message, SV2.length, uid.length);
        System.arraycopy(counter, 0, message, SV2.length + uid.length, counter.length);

        byte[] k2 = Hex.decode(k2Hex);

        byte[] mac1 = aesCmac(k2, message);
        byte[] mac2 = aesCmac(mac1, new byte[0]);

        // take every second byte (same as JS filter)
        byte[] truncated = new byte[8];
        for (int i = 0, j = 1; i < 8; i++, j += 2) {
            truncated[i] = mac2[j];
        }

        return Hex.toHexString(truncated);
    }

    private static byte[] aesCmac(byte[] key, byte[] message) {
        Mac mac = new CMac(new AESEngine());
        mac.init(new KeyParameter(key));
        mac.update(message, 0, message.length);
        byte[] out = new byte[mac.getMacSize()];
        mac.doFinal(out, 0);
        return out;
    }

    private static int bytesToInt(byte[] b) {
        return ((b[0] & 0xff) << 16)
                | ((b[1] & 0xff) << 8)
                | (b[2] & 0xff);
    }

}
