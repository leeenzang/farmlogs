package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.DashboardResponse;
import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.external.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final WeatherService weatherService;

    public Map<String, Object> getDashboardData() {
        WeatherResponse weather = weatherService.getTodayWeather();

        Map<String, Object> result = new HashMap<>();
        result.put("weather_raw", weather);

        return result;
    }
}