package com.ieunjin.farmlogs.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weather")
@Getter
@Setter
public class WeatherApiConfig {
    private String apiKey;
}