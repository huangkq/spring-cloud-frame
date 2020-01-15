package com.tech.base.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class LocalDateUtil extends DateFormatUtil {

    public static LocalDate currentLocalDate() {
        return LocalDate.now(DEFAULT_TIME_ZONE.toZoneId());
    }

    private static LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now(DEFAULT_TIME_ZONE.toZoneId());
    }

    public static String currentLocalDateStr() {
        return currentLocalDateStr(DATE_FORMAT_SHORT);
    }

    public static String currentLocalDateStr(String pattern) {
        LocalDate now = currentLocalDate();
        return localDateToString(now, pattern);
    }

    public static String currentLocalDateTimeStr() {
        return currentLocalDateTimeStr(DATE_FORMAT_FULL);
    }

    public static String currentLocalDateTimeStr(String pattern) {
        LocalDateTime now = currentLocalDateTime();
        return localDateTimeToString(now, pattern);
    }

    public static LocalTime ofLocalTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);
    }

    public static LocalDate ofLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static LocalDateTime ofLocalDateTime(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    /**
     * 说明:计算两个LocalDate 时间差，
     * 
     * @param unit 比较时间的单位枚举：DAYS(天) 等
     * @date 2020年1月14日
     */
    public static long localDateBetween(LocalDate localDate, LocalDate localDate2, ChronoUnit unit) {
        return Math.abs(localDate.until(localDate2, unit));
    }

    /** 计算LocalDate与当前的相隔天数 */
    public static long localDateBetween(LocalDate localDate) {
        return localDateBetween(localDate, currentLocalDate(), ChronoUnit.DAYS);
    }

    /**
     * 说明:计算两个LocalDateTime 时间差，
     * 
     * @param unit 比较时间的单位枚举：DAYS(天) 等
     * @date 2020年1月14日
     */
    public static long localDateTimeBetween(LocalDateTime localDateTime, LocalDateTime localDateTime2, ChronoUnit unit) {
        return Math.abs(localDateTime.until(localDateTime2, unit));
    }

    /** 计算两个LocalDateTime与当前的相隔天数 时间差 */
    public static long localDateTimeBetween(LocalDateTime localDateTime) {
        return localDateTimeBetween(localDateTime, currentLocalDateTime(), ChronoUnit.DAYS);
    }
}
