package com.ieunjin.farmlogs.external;

import com.ieunjin.farmlogs.dto.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherClient", url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0")
public interface WeatherClient {

    @GetMapping("/getUltraSrtNcst?ServiceKey=z%2BUvCqUF74ij9g%2FfyQQLXE0JrQohJmqtdfLHHxLC0LAjkMos00apQ9s3eqaa%2BK77jg8DEsRsQFfCeY6h070mgg%3D%3D")
    WeatherResponse getUltraSrtNcst(
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("dataType") String dataType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam("nx") int nx,
            @RequestParam("ny") int ny
    );

    @GetMapping("/getUltraSrtNcst?ServiceKey=z%2BUvCqUF74ij9g%2FfyQQLXE0JrQohJmqtdfLHHxLC0LAjkMos00apQ9s3eqaa%2BK77jg8DEsRsQFfCeY6h070mgg%3D%3D")
    String getUltraSrtNcstS(
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("dataType") String dataType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam("nx") int nx,
            @RequestParam("ny") int ny
    );

    @GetMapping("/getUltraSrtNcst?ServiceKey=z%2BUvCqUF74ij9g%2FfyQQLXE0JrQohJmqtdfLHHxLC0LAjkMos00apQ9s3eqaa%2BK77jg8DEsRsQFfCeY6h070mgg%3D%3D")
    WeatherResponse getVilageFcst(
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("dataType") String dataType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam("nx") int nx,
            @RequestParam("ny") int ny
    );
}