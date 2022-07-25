package com.rpdpymnt.reporting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class DateUtil {

    public static LocalDate convertStringToLocalDate(String dateTime) {
        LocalDate localDate = LocalDate.parse(dateTime);
        return localDate;
    }
}
