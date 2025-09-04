package com.ieunjin.farmlogs.external;

import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.dto.WeatherTodayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherResponse getTodayWeather() {
        String[] baseDateTime = DateTimeUtil.getBaseDateTime(); // 날짜 계산 유틸
        String baseDate = baseDateTime[0];
        String baseTime = baseDateTime[1];

        int nx = 60;
        int ny = 137;

        WeatherResponse response = weatherClient.getUltraSrtNcst(
                1, 10, "JSON", baseDate, baseTime, nx, ny);

        log.debug("날씨 API 응답: {}", response);
        return response;
    }

    public String getTodayWeatherRawJson() {
        String[] baseDateTime = DateTimeUtil.getBaseDateTime();
        String baseDate = baseDateTime[0];
        String baseTime = baseDateTime[1];

        return weatherClient.getUltraSrtNcstS(
                1, 10, "JSON", baseDate, baseTime, 60, 127);
    }


    public WeatherTodayDto fetchWeatherNow() {
        String[] base = DateTimeUtil.getCurrentBaseDateTime();
        WeatherResponse response = weatherClient.getUltraSrtNcst(
                1, 1000, "JSON", base[0], base[1], 60, 137
        );

        List<WeatherResponse.Item> items = response.getResponse().getBody().getItems().getItems();

        String temperature = getValue(items, "T1H");
        String humidity = getValue(items, "REH");
        String pty = getValue(items, "PTY");

        return WeatherTodayDto.builder()
                .temperature(temperature)
                .humidity(humidity)
                .weatherStatus(WeatherUtil.determineWeatherStatus(pty, null))
                .build();
    }

    private String getValue(List<WeatherResponse.Item> items, String category) {
        return items.stream()
                .filter(i -> category.equals(i.getCategory()) && i.getObsrValue() != null)
                .map(WeatherResponse.Item::getObsrValue)
                .findFirst()
                .orElse("정보 없음");
    }

}