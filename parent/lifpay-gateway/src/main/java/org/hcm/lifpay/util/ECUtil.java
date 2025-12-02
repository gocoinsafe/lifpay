package org.hcm.lifpay.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

/**
 * @author huaishuai
 */
@Slf4j
public class ECUtil {
    public final static String SECP256K1 = "secp256k1";
    public final static String SECP256R1 = "secp256r1";
    public final static int KEY_S_LEN = 33;
    public static final int EC_DSA_SIGNATURE_LEN = 64;
    public static final int EC_DSA_SIGNATURE_R_LEN = 32;
    public static final int EC_DSA_SIGNATURE_S_LEN = 32;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyPair genKeyPair(String curve) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", "BC");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curve);
            generator.initialize(ecGenParameterSpec);
            return generator.generateKeyPair();
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static PrivateKey convertPrvKey(byte[] prvKey, String curve) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            ECPrivateKeySpec prvSpec = new ECPrivateKeySpec(new BigInteger(1, prvKey), getEcParamSpec(curve));
            PrivateKey privateKey = keyFactory.generatePrivate(prvSpec);
            return privateKey;
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static byte[] convertPrvKey(PrivateKey prvKey) {
        try {
            ECPrivateKey ecPrivateKey = (ECPrivateKey) prvKey;
            byte[] encode = ecPrivateKey.getS().toByteArray();
            byte[] keyEncode = new byte[32];
            if (encode.length == KEY_S_LEN && encode[0] == 0) {
                System.arraycopy(encode, 1, keyEncode, 0, keyEncode.length);
            } else {
                keyEncode = encode;
            }
            return keyEncode;
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static PublicKey convertPubKey(byte[] pubKey, String curve) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            ECPoint point = sun.security.util.ECUtil.decodePoint(pubKey, getEcParamSpec(curve).getCurve());
            ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, getEcParamSpec(curve));
            PublicKey publicKey = keyFactory.generatePublic(pubSpec);
            return publicKey;
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static byte[] convertPubKey(PublicKey pubKey, String curve) {
        try {
            ECPublicKey ecPublicKey = (ECPublicKey) pubKey;
            return sun.security.util.ECUtil.encodePoint(ecPublicKey.getW(), getEcParamSpec(curve).getCurve());
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static ECPoint getEcPointFromPrvKey(byte[] prvKey, String curve) {
        try {
            BigInteger privateKey = new BigInteger(1, prvKey);
            if (privateKey.bitLength() > getParamSpec(curve).getN().bitLength()) {
                privateKey = privateKey.mod(getParamSpec(curve).getN());
            }
            org.bouncycastle.math.ec.ECPoint bcEcPoint = (new FixedPointCombMultiplier()).multiply(getParamSpec(curve).getG(),
                    privateKey);
            ECPoint point = sun.security.util.ECUtil.decodePoint(bcEcPoint.getEncoded(false), getEllipticCurve(curve));
            return point;
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static byte[] getPubKeyFromPrvKey(byte[] prvKey, String curve) {
        return getPubKeyFromPrvKey(prvKey, false, curve);
    }

    public static byte[] getPubKeyFromPrvKey(byte[] prvKey, boolean compressed, String curve) {
        try {
            ECPoint point = getEcPointFromPrvKey(prvKey, curve);
            org.bouncycastle.math.ec.ECPoint bcEcPoint = EC5Util.convertPoint(getParamSpec(curve).getCurve(), point);
            return bcEcPoint.getEncoded(compressed);
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static KeyPair genKeyPairByPrvKey(byte[] prvKey, String curve) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            ECParameterSpec ecParameterSpec = getEcParamSpec(curve);
            PrivateKey privateKey = convertPrvKey(prvKey, curve);

            ECPoint point = getEcPointFromPrvKey(prvKey, curve);
            ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecParameterSpec);
            PublicKey publicKey = keyFactory.generatePublic(pubSpec);

            KeyPair keyPair = new KeyPair(publicKey, privateKey);
            return keyPair;
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static BigInteger[] decodeFromDer(byte[] bytes) {
        try {
            ASN1InputStream decoder = new ASN1InputStream(bytes);
            ASN1Primitive seqObj = decoder.readObject();
            DLSequence seq = (DLSequence) seqObj;
            ASN1Integer r;
            ASN1Integer s;
            r = (ASN1Integer) seq.getObjectAt(0);
            s = (ASN1Integer) seq.getObjectAt(1);
            BigInteger[] signature = new BigInteger[2];
            signature[0] = r.getPositiveValue();
            signature[1] = s.getPositiveValue();
            return signature;
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static byte[] sign(String msg, String prvKey, String cure) {
        return sign(HashUtil.sha256(msg.getBytes()), Hex.decode(prvKey), cure);
    }

    public static byte[] sign(byte[] msg, byte[] prvKey, String cure) {
        try {
            Signature sign = Signature.getInstance("SHA256withECDSA", "BC");
            PrivateKey privateKey = convertPrvKey(prvKey, cure);
            sign.initSign(privateKey);
            sign.update(msg);
            return sign.sign();
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static boolean verify(String msg, String signature, String pubKey, String cure) {
        return verify(HashUtil.sha256(msg.getBytes()), Hex.decode(signature), Hex.decode(pubKey), cure);
    }

    public static boolean verify(byte[] msg, byte[] signature, byte[] pubKey, String cure) {
        try {
            Signature sign = Signature.getInstance("SHA256withECDSA", "BC");
            PublicKey publicKey = convertPubKey(pubKey, cure);
            sign.initVerify(publicKey);
            sign.update(msg);
            return sign.verify(signature);
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return false;
    }

    public static byte[] encrypt(String content, String publicKey, String cure) {
        return encrypt(content.getBytes(), Hex.decode(publicKey), cure);
    }

    public static byte[] encrypt(byte[] content, byte[] publicKey, String cure) {
        try {
            PublicKey pubKey = convertPubKey(publicKey, cure);
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    public static byte[] decrypt(String content, String privateKey, String cure) {
        return decrypt(Hex.decode(content), Hex.decode(privateKey), cure);
    }

    public static byte[] decrypt(byte[] content, byte[] privateKey, String cure) {
        try {
            PrivateKey prvKey = convertPrvKey(privateKey, cure);
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.DECRYPT_MODE, prvKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("Fail:", e);
        }
        return null;
    }

    private static EllipticCurve getEllipticCurve(String curve) {
        EllipticCurve ellipticCurve = EC5Util.convertCurve(getParamSpec(curve).getCurve(), getParamSpec(curve).getSeed());
        return ellipticCurve;
    }

    private static ECNamedCurveParameterSpec getParamSpec(String curve) {
        return ECNamedCurveTable.getParameterSpec(curve);
    }

    private static ECParameterSpec getEcParamSpec(String curve) {
        ECNamedCurveParameterSpec ecNamedCurveParameterSpec = getParamSpec(curve);
        EllipticCurve ellipticCurve = EC5Util.convertCurve(ecNamedCurveParameterSpec.getCurve(), ecNamedCurveParameterSpec.getSeed());
        ECPoint g = ECPointUtil.decodePoint(ellipticCurve, ecNamedCurveParameterSpec.getG().getEncoded(false));
        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g, ecNamedCurveParameterSpec.getN(),
                ecNamedCurveParameterSpec.getH().intValue());
        return ecParameterSpec;
    }

}
