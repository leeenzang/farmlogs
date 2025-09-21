package com.ieunjin.farmlogs.common;

import com.ieunjin.farmlogs.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherService weatherService;

    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void refreshWeatherCache() {
        log.info("스케줄러 실행 → 날씨 캐시 갱신 시작 at {}", java.time.LocalDateTime.now());

        var now = weatherService.updateWeatherNow();
        log.info("updateWeatherNow 완료: {}, {}", now.getTemperature(), now.getWeatherStatus());

        var tomorrow = weatherService.updateWeatherTomorrow();
        log.info("updateWeatherTomorrow 완료: 최고 {}°C / 최저 {}°C / 상태 {}",
                tomorrow.getHighestTemp(),
                tomorrow.getLowestTemp(),
                tomorrow.getWeatherStatus());

        log.info("스케줄러 실행 → 날씨 캐시 갱신 완료 at {}", java.time.LocalDateTime.now());
    }
}