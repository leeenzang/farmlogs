package com.ieunjin.farmlogs.external;

import com.ieunjin.farmlogs.dto.WeatherResponse;
import com.ieunjin.farmlogs.dto.WeatherTodayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherResponse getTodayWeather() {
        String[] baseDateTime = DateTimeUtil.getBaseDateTime(); // 날짜 계산 유틸
        String baseDate = baseDateTime[0];
        String baseTime = baseDateTime[1];

        int nx = 60;
        int ny = 137;

        WeatherResponse response = weatherClient.getUltraSrtNcst(
                1, 10, "JSON", baseDate, baseTime, nx, ny);

        log.debug("날씨 API 응답: {}", response);
        return response;
    }

    public String getTodayWeatherRawJson() {
        String[] baseDateTime = DateTimeUtil.getBaseDateTime();
        String baseDate = baseDateTime[0];
        String baseTime = baseDateTime[1];

        return weatherClient.getUltraSrtNcstS(
                1, 10, "JSON", baseDate, baseTime, 60, 127);
    }


    public Map<String, String> fetchWeatherNow() {
        String[] base = DateTimeUtil.getCurrentBaseDateTime(); // 실황용 날짜/시간
        WeatherResponse response = weatherClient.getUltraSrtNcst(
                1, 1000, "JSON", base[0], base[1], 60, 137
        );

        List<WeatherResponse.Item> items = response.getResponse().getBody().getItems().getItems();
        Set<String> targetCats = Set.of("T1H", "REH", "PTY");

        Map<String, String> result = items.stream()
                .filter(i -> targetCats.contains(i.getCategory()))
                .collect(Collectors.toMap(
                        WeatherResponse.Item::getCategory,
                        WeatherResponse.Item::getObsrValue,
                        (v1, v2) -> v2 // 중복 시 최신값 유지
                ));

        String pty = result.get("PTY");
        String weatherStatus = WeatherUtil.determineWeatherStatus(pty, null);
        result.put("weatherStatus", weatherStatus);

        return result;
    }

}