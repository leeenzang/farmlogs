package com.ieunjin.farmlogs.external;

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

    public static String[] getCurrentBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();

        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = now.format(DateTimeFormatter.ofPattern("HH")) + "00";

        return new String[]{ baseDate, baseTime };
    }

    public static String[] getForecastBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        List<Integer> baseHours = List.of(2, 5, 8, 11, 14, 17, 20, 23);
        int hour = now.getHour();

        int baseHour = baseHours.stream().filter(h -> h <= hour).reduce((f, s) -> s).orElse(23);
        LocalDate baseDate = (baseHour == 23 && hour < 2)
                ? now.minusDays(1).toLocalDate()
                : now.toLocalDate();

        return new String[]{
                baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                String.format("%02d00", baseHour)
        };
    }
}
