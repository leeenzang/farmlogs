package com.ieunjin.farmlogs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDto {
    private String sky;       // 맑음, 흐림 등
    private String temperature;
    private String humidity;
    private String precipitationType; // 비/눈/없음 등
}