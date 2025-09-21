package com.ieunjin.farmlogs.controller;

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
    public WeatherTodayDto getNowWeather() {
        log.info("오늘 날씨 조회 요청");
        return weatherService.getWeatherNow();
    }

    @GetMapping("/tomorrow")
    public WeatherTomorrowDto getTomorrowWeather() {
        log.info("내일 날씨 조회 요청");
        return weatherService.getWeatherTomorrow();
    }
}