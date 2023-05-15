package com.gitlab.juli220620.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static LocalDateTime convertToLocalDateTimeViaInstant(java.sql.Date dateToConvert) {
        return convertToLocalDateTimeViaInstant(new Date(dateToConvert.getTime()));
    }
    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMillisecond(java.sql.Date dateToConvert) {
        return convertToLocalDateTimeViaMillisecond(new Date(dateToConvert.getTime()));
    }
    public static LocalDateTime convertToLocalDateTimeViaMillisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
