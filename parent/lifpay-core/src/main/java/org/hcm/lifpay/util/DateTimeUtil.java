package org.hcm.lifpay.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author jerry
 */
@Slf4j
public class DateTimeUtil {
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATETIME_SHORT = "yyyyMMddHHmmss";
    public static final String FORMAT_DATE_SHORT = "yyyyMMdd";

    /**
     * 默认时区：东八区（Asia/Shanghai，适配国内场景）
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");

    public static String dateTimeToStr(LocalDateTime dateTime, String format) {
        try {
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(format);
            return dateTime.format(dtFormatter);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return dateTime.format(DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
    }

    public static String dateTimeToStr(Date dateTime) {
        return dateTimeToStr(dateTime, FORMAT_DEFAULT);
    }

    public static String dateTimeToStr(Date dateTime, String format) {
        try {
            SimpleDateFormat dtFormatter = new SimpleDateFormat(format);
            return dtFormatter.format(dateTime);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return new SimpleDateFormat(FORMAT_DEFAULT).format(dateTime);
    }

    public static String dateTimeToStr(LocalDateTime dateTime) {
        return dateTimeToStr(dateTime, FORMAT_DEFAULT);
    }

    /**
     * UTC时间转成其他时区时间
     *
     * @param utcDateTime
     * @return UTC时间
     */
    public static LocalDateTime utcDateTimeToGMT(Date utcDateTime, int offset) {
        int offsetHour = getOffsetHour();
        if (offsetHour == offset) {
            // 如果当前系统时区和目标时区一致，返回
            return utcDateTime.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime();
        } else if (offsetHour == 0) {
            // 如果系统是0时区，返回目标时区时间
            return utcDateTime.toInstant().atOffset(ZoneOffset.UTC).withOffsetSameInstant(ZoneOffset.ofHours(offset)).toLocalDateTime();
        } else {
            // 如果系统是其他时区，返回目标时区时间
            return utcDateTime.toInstant().atOffset(ZoneOffset.ofHours(offsetHour)).withOffsetSameInstant(ZoneOffset.ofHours(offset)).toLocalDateTime();
        }
    }

    /**
     * UTC时间转成东8区时间
     *
     * @param utcDateTime
     * @return UTC时间
     */
    public static LocalDateTime utcDateTimeToGMT8(Date utcDateTime) {
        return utcDateTimeToGMT(utcDateTime, 8);
    }

    public static int getOffsetHour() {
        return TimeZone.getDefault().getRawOffset() / 1000 / 60 / 60;
    }

    public static Date addYear(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    /**
     * 核心方法：毫秒时间戳 → 年月日时分秒字符串（默认格式+默认时区）
     * @param timestamp 毫秒级时间戳（Long类型，可为null）
     * @return 格式化后的日期字符串，异常时返回null
     */
    public static String timestampToStr(Long timestamp) {
        return timestampToStr(timestamp, FORMAT_DEFAULT, DEFAULT_ZONE_ID);
    }

    /**
     * 重载方法：毫秒时间戳 → 自定义格式字符串（默认时区）
     * @param timestamp 毫秒级时间戳（Long类型，可为null）
     * @param pattern 自定义日期格式（如 "yyyy-MM-dd HH:mm:ss.SSS" 含毫秒，"yyyy年MM月dd日 HH时mm分ss秒"）
     * @return 格式化后的日期字符串，异常时返回null
     */
    public static String timestampToStr(Long timestamp, String pattern) {
        return timestampToStr(timestamp, pattern, DEFAULT_ZONE_ID);
    }

    /**
     * 全参数方法：毫秒时间戳 → 自定义格式+自定义时区字符串（最灵活）
     * @param timestamp 毫秒级时间戳（Long类型，可为null）
     * @param pattern 自定义日期格式（如 "yyyy-MM-dd HH:mm:ss"）
     * @param zoneId 时区（如 ZoneId.of("UTC")、ZoneId.of("Asia/Tokyo")）
     * @return 格式化后的日期字符串，异常时返回null
     */
    public static String timestampToStr(Long timestamp, String pattern, ZoneId zoneId) {
        // 1. 校验入参
        if (timestamp == null || timestamp < 0) {
            return null; // 非法时间戳（null/负数）返回null，也可根据需求返回空串""
        }
        if (pattern == null || pattern.trim().isEmpty()) {
            pattern = FORMAT_DEFAULT; // 格式为空时使用默认格式
        }
        if (zoneId == null) {
            zoneId = DEFAULT_ZONE_ID; // 时区为空时使用默认东八区
        }

        try {
            // 2. 时间戳 → LocalDateTime（通过Instant转时区）
            LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(zoneId) // 指定时区
                    .toLocalDateTime(); // 转为本地日期时间

            // 3. 格式化日期（DateTimeFormatter线程安全，可复用）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return localDateTime.format(formatter);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            // 捕获格式错误、时区错误等异常（避免程序崩溃）
            return null;
        }
    }

    // -------------------------- 以下是反向转换（可选，方便复用）--------------------------
    /**
     * 年月日时分秒字符串 → 毫秒时间戳（默认格式+默认时区）
     * @param dateStr 日期字符串（如 "2025-11-18 15:30:00"）
     * @return 毫秒级时间戳，异常时返回null
     */
    public static Long strToTimestamp(String dateStr) {
        return strToTimestamp(dateStr, FORMAT_DEFAULT, DEFAULT_ZONE_ID);
    }

    /**
     * 年月日时分秒字符串 → 毫秒时间戳（自定义格式+自定义时区）
     * @param dateStr 日期字符串
     * @param pattern 日期格式（需与dateStr匹配）
     * @param zoneId 时区
     * @return 毫秒级时间戳，异常时返回null
     */
    public static Long strToTimestamp(String dateStr, String pattern, ZoneId zoneId) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        if (pattern == null || pattern.trim().isEmpty()) {
            pattern = FORMAT_DEFAULT;
        }
        if (zoneId == null) {
            zoneId = DEFAULT_ZONE_ID;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
            // 转为时间戳（毫秒）
            return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return null;
        }
    }



    public static void main(String[] args) {


        String tt = timestampToStr(System.currentTimeMillis());
        System.out.println(tt);

    }


}
