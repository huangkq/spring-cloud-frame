package com.tech.base.utils.date;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DateFormatUtil {

    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_COMPACT = "yyyyMMdd";
    public static final String DATE_FORMAT_COMPACTFULL = "yyyyMMddHHmmss";
    public static final String DATE_YEAR_MONTH = "yyyyMM";
    public static final String DATE_FORMAT_FULL_MSE = "yyyyMMddHHmmssSSS";
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    public static String localDateToString(LocalDate now, String pattern) {
        return now.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateTimeToString(LocalDateTime now, String pattern) {
        return now.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return instantToDate(localDateTime.atZone(DEFAULT_TIME_ZONE.toZoneId()).toInstant());
    }

    public static Date localDateToDate(LocalDate localDate) {
        return instantToDate(localDate.atStartOfDay(DEFAULT_TIME_ZONE.toZoneId()).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_TIME_ZONE.toZoneId()).toLocalDateTime();
    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(DEFAULT_TIME_ZONE.toZoneId()).toLocalDate();
    }

    public static Date instantToDate(Instant instant) {
        return Date.from(instant);
    }

    public static String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static LocalDateTime stringToLocalDateTime(String date) {
        return stringToLocalDateTime(date, DATE_FORMAT_FULL);
    }

    public static LocalDateTime stringToLocalDateTime(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    public static LocalDate stringToLocalDate(String date) {
        return stringToLocalDate(date, DATE_FORMAT_FULL);
    }

    public static LocalDate stringToLocalDate(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    public static Date stringToDate(String date, String pattern) {
        if (Objects.equals(pattern, DATE_FORMAT_SHORT)) {
            return localDateToDate(stringToLocalDate(date, pattern));
        } else {
            return localDateTimeToDate(stringToLocalDateTime(date, pattern));
        }
    }
}
