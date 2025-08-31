package com.ieunjin.farmlogs.common;

import com.github.fj.koreanlunarcalendar.KoreanLunarCalendarUtils;
import com.github.fj.koreanlunarcalendar.KoreanLunarDate;

import java.time.LocalDate;

public class LunarDateUtil {

    public static String calculateLunarDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        KoreanLunarDate lunar = KoreanLunarCalendarUtils.getLunarDateOf(year, month, day);

        String leapTag = lunar.isLunLeapMonth ? " (윤달)" : "";

        return String.format("%d-%02d-%02d%s",
                lunar.lunYear,
                lunar.lunMonth,
                lunar.lunDay,
                leapTag
        );
    }
}