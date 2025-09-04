package com.ieunjin.farmlogs.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DateTimeUtil {

    public static String[] getBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int[] baseHours = {2, 5, 8, 11, 14, 17, 20, 23};

        int baseHour = Arrays.stream(baseHours)
                .filter(h -> h <= hour)
                .max()
                .orElse(23);

        LocalDate baseDate = (hour < baseHours[0])
                ? now.minusDays(1).toLocalDate()
                : now.toLocalDate();

        String baseTime = String.format("%02d00", baseHour);
        return new String[]{
                baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                baseTime
        };
    }

    public static String getClosestFcstTime() {
        int nowMin = LocalDateTime.now().getMinute();
        int nowHour = LocalDateTime.now().getHour();

        // 현재 시간에서 30분 기준으로 반올림
        if (nowMin >= 30) {
            nowHour += 1;
        }

        return String.format("%02d00", nowHour);
    }

    public static String getUltraSrtFcstBaseTime() {
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int hour = now.getHour();

        // 기준 시각이 30분 단위니까 이전 기준 시간으로 보정
        if (minute < 45) {
            if (hour == 0) {
                return "2330"; // 전날 마지막 시간
            }
            hour -= 1;
            return String.format("%02d30", hour);
        } else {
            return String.format("%02d30", hour);
        }
    }

}
