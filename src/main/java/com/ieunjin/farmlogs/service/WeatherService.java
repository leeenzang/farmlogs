package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.common.DateTimeUtil;
import com.ieunjin.farmlogs.common.WeatherUtil;
import com.ieunjin.farmlogs.config.WeatherApiConfig;
import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.dto.WeatherTodayDto;
import com.ieunjin.farmlogs.dto.WeatherTomorrowDto;
import com.ieunjin.farmlogs.external.WeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final int GRID_X = 60;
    private static final int GRID_Y = 137;

    private final WeatherClient weatherClient;
    private final WeatherApiConfig weatherApiConfig;


    // 오늘 날씨
    @CachePut(value = "weatherNow", key = "'now'")
    public WeatherTodayDto updateWeatherNow() {
        log.info("updateWeatherNow 실행");
        return callWeatherNowApi();
    }

    @Cacheable(value = "weatherNow", key = "'now'")
    public WeatherTodayDto getWeatherNow() {
        log.info("getWeatherNow 실행");
        return callWeatherNowApi();
    }

    private WeatherTodayDto callWeatherNowApi() {
        String apiKey = weatherApiConfig.getApiKey();
        String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = DateTimeUtil.getUltraSrtFcstBaseTime();

        WeatherResponse response = weatherClient.getUltraSrtFcst(
                apiKey,
                1, 1000, "JSON", baseDate, baseTime, GRID_X, GRID_Y
        );

        List<WeatherResponse.Item> items = response.getResponse().getBody().getItems().getItems();
        String temperature = getValue(items, "T1H");
        String humidity = getValue(items, "REH");
        String pty = getValue(items, "PTY");
        String sky = getValue(items, "SKY");

        return WeatherTodayDto.builder()
                .temperature(temperature)
                .humidity(humidity)
                .weatherStatus(WeatherUtil.determineWeatherStatus(pty, sky))
                .build();
    }

    // 내일 날씨
    @CachePut(value = "weatherTomorrow", key = "'tomorrow'")
    public WeatherTomorrowDto updateWeatherTomorrow() {
        log.info("updateWeatherTomorrow 실행 (API 호출 + 캐시 갱신)");
        return callWeatherTomorrowApi();
    }

    @Cacheable(value = "weatherTomorrow", key = "'tomorrow'")
    public WeatherTomorrowDto getWeatherTomorrow() {
        log.info("getWeatherTomorrow 실행 (캐시에 없을 때만 API 호출)");
        return callWeatherTomorrowApi();
    }


    private WeatherTomorrowDto callWeatherTomorrowApi() {
        String apiKey = weatherApiConfig.getApiKey();
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String[] baseDateTime = DateTimeUtil.getBaseDateTime();

        WeatherResponse response = weatherClient.getVilageFcst(
                apiKey,
                1, 1000, "JSON", baseDateTime[0], baseDateTime[1], GRID_X, GRID_Y
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

    // 공통 로직
    private String getValue(List<WeatherResponse.Item> items, String category) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String closestTime = DateTimeUtil.getClosestFcstTime();

        return items.stream()
                .filter(i -> category.equals(i.getCategory()) &&
                        today.equals(i.getFcstDate()) &&
                        closestTime.equals(i.getFcstTime()))
                .map(WeatherResponse.Item::getFcstValue)
                .findFirst()
                .orElse("정보 없음");
    }

    private String getForecastValue(List<WeatherResponse.Item> items, String category, String fcstDate) {
        return items.stream()
                .filter(i -> category.equals(i.getCategory()) && fcstDate.equals(i.getFcstDate()))
                .map(WeatherResponse.Item::getFcstValue)
                .findFirst()
                .orElse("정보 없음");
    }
}