package org.hcm.lifpay.util;


import org.bouncycastle.util.encoders.Hex;

/**
 * ECUtil 签名生成+验签验证单元测试
 * 测试数据：
 * 原始数据：{"timestamp":1768552538912,"requestId":"oc241pZcwu2USQV60i0e9Szg1a7MXkjJ"}
 * 私钥（16进制）：e24fd5fdf2359e7a009c6113eab570f8723018d1dcbbc3359260854db63061e0
 * 公钥（16进制）：048902cf7fd05f25ea91d3ecdc34b01be6efcd7b9cd6824f645311b3740e61eb1dc70e52363f65ec1dec2a5c343b8d32eebda3c345050ff25b6cfc3bf4147a8b98
 * 曲线类型：SECP256K1（与生成密钥对的曲线一致）
 */
public class ECUtilSignTest {

    // 1. 定义测试常量（直接复用你提供的数据）
    private static final String RAW_DATA = "Hello, Java!";
//    private static final String PRIVATE_KEY_HEX = "5b6fea114f3ca4505131da8b1ca0d6b6b2c13046eb1e8a4b63330c44ae5f14c4";
//    private static final String PUBLIC_KEY_HEX = "046769d15c6e2c22ebd61b0c399c065b277212077d27fe34f6b66b98c78afeb8a3746e141da87a5ab2a85bd72ff9f78e1524e77a232187f2391700e10ef7b5fa24";

    private static final String PRIVATE_KEY_HEX = "5b6fea114f3ca4505131da8b1ca0d6b6b2c13046eb1e8a4b63330c44ae5f14c4";
    private static final String PUBLIC_KEY_HEX = "046769d15c6e2c22ebd61b0c399c065b277212077d27fe34f6b66b98c78afeb8a3746e141da87a5ab2a85bd72ff9f78e1524e77a232187f2391700e10ef7b5fa24";
    private static final String CURVE_TYPE = ECUtil.SECP256R1; // 密钥对生成用的是SECP256K1，必须匹配

    /**
     * 核心测试：生成签名 + 验签验证
     * 步骤：
     * 1. 调用ECUtil.sign生成签名
     * 2. 打印生成的sign值（供你核对）
     * 3. 调用ECUtil.verify验证签名有效性
     */

    public static void testGenerateAndVerifySign() {
        // ========== 步骤1：生成签名 ==========
//        byte[] signBytes1 = ECUtil.sign(RAW_DATA, PRIVATE_KEY_HEX, CURVE_TYPE);
//        // 转换为16进制字符串（便于查看和后续使用）
////        String signHex = Hex.toHexString(signBytes);
//        String backHex = Hex.toHexString(signBytes1);;
//        System.out.println("后端："+ backHex);
//
////        String signHex = "30450220761d4fc10d6a21ef498fbb0ce5fcc59441291e632d1687a16c069890ef4d8d0e022100897ac1c5f5be30f0988650d5ba76c4a67ccc9b869add3c513b235355df7d7fc0";
//
//        // 打印关键信息（方便你核对结果）
//        System.out.println("======= 签名生成结果 =======");
//        System.out.println("原始数据：" + RAW_DATA);
//        System.out.println("私钥（16进制）：" + PRIVATE_KEY_HEX);
//        System.out.println("生成的签名（16进制）：" + backHex);
//        System.out.println("===========================");
//
//        // 校验签名生成是否成功（避免空指针）
//        System.out.println("签名生成失败：signBytes为null:"+  backHex);
//        System.out.println("签名生成失败：signBytes为空:"+ signHex.length);

        // ========== 步骤2：验签验证 ==========
        boolean verifyResult = ECUtil.verify(RAW_DATA, "3045022100a5f342e0ff10f04ef834831e818e1a2c158e796a8a00f3e3dab5945f90ba89020220143f9a01319532e88aabe0100d3b3235f3adab008b971aef8a88fda7f523e0fd", PUBLIC_KEY_HEX, CURVE_TYPE);

        // 打印验签结果
        System.out.println("======= 验签结果 =======" + verifyResult );

    }


    public static void main(String[] args) {
        testGenerateAndVerifySign();
    }

}
