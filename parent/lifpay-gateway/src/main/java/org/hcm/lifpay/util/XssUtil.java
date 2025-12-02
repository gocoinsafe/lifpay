package org.hcm.lifpay.util;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;



public class XssUtil {

    private static final Logger log = LoggerFactory.getLogger(XssUtil.class);
    private static final Pattern[] SCRIPT_PATTERNS = new Pattern[]{Pattern.compile("<script>(.*?)</script>", 2), Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42), Pattern.compile("</script>", 2), Pattern.compile("<script(.*?)>", 42), Pattern.compile("eval\\((.*?)\\)", 42), Pattern.compile("expression\\((.*?)\\)", 42), Pattern.compile("javascript:", 2), Pattern.compile("vbscript:", 2), Pattern.compile("onload(.*?)=", 42)};

    public XssUtil() {
    }

    public static String cleanObj(String val) {
        JSONObject jsonObject = JSONObject.parseObject(val);
        cleanObj(jsonObject);
        return jsonObject.toJSONString();
    }

    public static void cleanObj(JSONObject jsonObject) {
        Iterator var1 = jsonObject.entrySet().iterator();

        while (var1.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) var1.next();
            if (entry.getValue() != null && entry.getValue() instanceof String) {
                String str = (String) entry.getValue();
                str = replaceXss(str);
                entry.setValue(str);
            }
        }

    }

    private static String replaceXss(String value) {
        if (value != null) {
            value = value.replaceAll("\u0000", "");
            Pattern[] var1 = SCRIPT_PATTERNS;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Pattern pattern = var1[var3];
                value = pattern.matcher(value).replaceAll("");
            }
        }

        return value;
    }
}
