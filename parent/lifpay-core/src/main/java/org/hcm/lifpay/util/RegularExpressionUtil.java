package org.hcm.lifpay.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏/提取工具类（邮箱+手机号）
 */
public class RegularExpressionUtil {


    // 1. 提取邮箱前缀
    public static String extractEmailPrefix(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }
        Pattern pattern = Pattern.compile("^(.+)@");
        Matcher matcher = pattern.matcher(email);
        return matcher.find() ? matcher.group(1) : "";
    }

    // 2. 邮箱前缀脱敏
    public static String desensitizeEmail(String email) {
        if (email == null || !email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            return email; // 非合法邮箱直接返回
        }
        return email.replaceAll("^(.{1})(.*)(.{0,1})@", "$1****$3@");
    }

    // 3. 提取手机号后四位
    public static String extractMobileLast4(String mobile) {
        if (mobile == null || !mobile.matches("^\\d{11}$")) {
            return ""; // 非11位手机号返回空
        }
        Pattern pattern = Pattern.compile("\\d{7}(\\d{4})$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.find() ? matcher.group(1) : "";
    }

    // 4. 手机号脱敏（保留前3后4）
    public static String desensitizeMobile(String mobile) {
        if (mobile == null || !mobile.matches("^\\d{11}$")) {
            return mobile; // 非合法手机号直接返回
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    // 测试示例
    public static void main(String[] args) {
        // 邮箱测试
        String email = "zhangsan123@163.com";
        System.out.println("邮箱前缀：" + extractEmailPrefix(email)); // 输出：zhangsan123
        System.out.println("脱敏邮箱：" + desensitizeEmail(email)); // 输出：z****3@163.com

        // 手机号测试
        String mobile = "13812345678";
        System.out.println("手机号后四位：" + extractMobileLast4(mobile)); // 输出：5678
        System.out.println("脱敏手机号：" + desensitizeMobile(mobile)); // 输出：138****5678
    }
}
