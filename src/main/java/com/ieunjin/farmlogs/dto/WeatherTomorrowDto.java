package com.ieunjin.farmlogs.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherTomorrowDto {
    private String weatherStatus;           // SKY + PTY 조합
    private String precipitationProbability; // POP
    private String lowestTemp;              // TMN
    private String highestTemp;             // TMX
}