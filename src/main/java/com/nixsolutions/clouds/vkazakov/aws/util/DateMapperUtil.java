package com.nixsolutions.clouds.vkazakov.aws.util;

import java.sql.Date;

public class DateMapperUtil {
    public static String map(Date date) {
        if (date == null) {
            return "";
        }
        return date.toLocalDate().toString();
    }

    public static Date map(String value) {
        if (value == null || value.equals("")) {
            return null;
        }
        return Date.valueOf(value);
    }
}
