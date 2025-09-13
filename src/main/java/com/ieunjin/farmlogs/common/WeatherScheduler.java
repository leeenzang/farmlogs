package com.ieunjin.farmlogs.common;

import com.ieunjin.farmlogs.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherService weatherService;

    @Async
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void refreshWeatherCache() {
        log.info("스케줄러 실행 → 날씨 캐시 갱신 시작");

        // 캐시 적용된 메서드 호출, 값이 자동으로 캐시에 저장
        weatherService.fetchWeatherNow();
        weatherService.fetchWeatherTomorrow();

        log.info("스케줄러 실행 → 날씨 캐시 갱신 완료");
    }
}