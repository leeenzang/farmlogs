package com.ieunjin.farmlogs.common;

public class WeatherUtil {
    public static String determineWeatherStatus(String ptyCode, String skyCode) {
        if (ptyCode != null && !ptyCode.equals("0")) {
            return switch (ptyCode) {
                case "1" -> "비";
                case "2" -> "비/눈";
                case "3" -> "눈";
                case "4" -> "소나기";
                case "5" -> "빗방울";
                case "6" -> "빗방울눈날림";
                case "7" -> "눈날림";
                default -> "강수";
            };
        }

        if (skyCode != null) {
            return switch (skyCode) {
                case "1" -> "맑음";
                case "3" -> "구름많음";
                case "4" -> "흐림";
                default -> "알 수 없음";
            };
        }

        // 둘 다 null일 경우 방어
        return "정보 없음";
    }
}