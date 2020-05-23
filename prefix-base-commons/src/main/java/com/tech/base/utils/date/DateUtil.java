package com.tech.base.utils.date;

import java.util.Date;

public class DateUtil extends LocalDateUtil {

    public static Date currentDate() {
        return new Date();
    }

    public static String currentDateStr() {
        return currentDateStr(DATE_FORMAT_FULL);
    }

    public static String currentDateStr(String pattern) {
        return dateToString(currentDate(), pattern);
    }
}
