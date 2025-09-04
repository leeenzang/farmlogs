package com.ieunjin.farmlogs.external;

import com.ieunjin.farmlogs.dto.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherClient", url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0")
public interface WeatherClient {

    @GetMapping("/getUltraSrtFcst")
    WeatherResponse getUltraSrtFcst(
            @RequestParam("ServiceKey") String serviceKey,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("dataType") String dataType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam("nx") int nx,
            @RequestParam("ny") int ny
    );


    // 내일날씨
    @GetMapping("/getVilageFcst")
    WeatherResponse getVilageFcst(
            @RequestParam("ServiceKey") String serviceKey,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("dataType") String dataType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam("nx") int nx,
            @RequestParam("ny") int ny
    );

}