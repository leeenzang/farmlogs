package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.dto.WeatherTodayDto;
import com.ieunjin.farmlogs.dto.WeatherTomorrowDto;
import com.ieunjin.farmlogs.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/weather")
@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/today")
    public WeatherTodayDto fetchNowWeather() {
        log.info("GET /api/weather/today 진입");
        WeatherTodayDto dto = weatherService.fetchWeatherNow();
        return dto;
    }

    @GetMapping("/tomorrow")
    public WeatherTomorrowDto fetchTomorrowWeather() {
        log.info("GET /api/weather/tomorrow 진입");
        WeatherTomorrowDto dto = weatherService.fetchWeatherTomorrow();
        return dto;
    }
}