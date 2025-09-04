package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.common.DateTimeUtil;
import com.ieunjin.farmlogs.common.WeatherUtil;
import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.dto.WeatherTodayDto;
import com.ieunjin.farmlogs.dto.WeatherTomorrowDto;
import com.ieunjin.farmlogs.external.WeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;

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

    public WeatherTomorrowDto fetchWeatherTomorrow() {
        String tomorrow = LocalDate.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        WeatherResponse response = weatherClient.getVilageFcst(
                1, 1000, "JSON",
                DateTimeUtil.getBaseDateTime()[0],  // base_date
                DateTimeUtil.getBaseDateTime()[1],  // base_time
                60, 137
        );

        List<WeatherResponse.Item> items = response.getResponse().getBody().getItems().getItems();

        String sky = getForecastValue(items, "SKY", tomorrow);
        String pty = getForecastValue(items, "PTY", tomorrow);

        return WeatherTomorrowDto.builder()
                .weatherStatus(WeatherUtil.determineWeatherStatus(pty, sky))
                .precipitationProbability(getForecastValue(items, "POP", tomorrow))
                .lowestTemp(getForecastValue(items, "TMN", tomorrow))
                .highestTemp(getForecastValue(items, "TMX", tomorrow))
                .build();
    }

    private String getForecastValue(List<WeatherResponse.Item> items, String category, String fcstDate) {
        return items.stream()
                .filter(i -> category.equals(i.getCategory()) && fcstDate.equals(i.getFcstDate()))
                .map(WeatherResponse.Item::getFcstValue)
                .findFirst()
                .orElse("정보 없음");
    }


}