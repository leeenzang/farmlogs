package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.WeatherDto;
import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.external.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/api/weather")
    public WeatherResponse getWeather() {
        WeatherResponse response = weatherService.getTodayWeather();
        return response;
    }

    @GetMapping("/api/weatherS")
    public String getWeatherRaw() {
        String response = weatherService.getTodayWeatherRawJson();
        return response;
    }

    @GetMapping("/api/weather-now")
    public Map<String, String> fetchNowWeather() {
        return weatherService.fetchWeatherNow();
    }

    @GetMapping("/api/weather-forecast")
    public Map<String, String> getForecast(@RequestParam String targetDate) {
        return weatherService.fetchWeatherForecast(targetDate);
    }
}