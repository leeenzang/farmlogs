package com.ieunjin.farmlogs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherTodayDto {
    private String weatherStatus;  // 흐림, 비, 맑음 등
    private String temperature;    // T1H
    private String humidity;       // REH
}