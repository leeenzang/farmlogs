package com.ieunjin.farmlogs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
    }

    @Data
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Data
    public static class Body {
        @JsonProperty("dataType")
        private String dataType;

        @JsonProperty("items")
        private Items items;

        @JsonProperty("pageNo")
        private String pageNo;

        @JsonProperty("numOfRows")
        private String numOfRows;

        @JsonProperty("totalCount")
        private String totalCount;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<Item> items;
    }

    @Data
    public static class Item {
        @JsonProperty("baseDate")
        private String baseDate;

        @JsonProperty("baseTime")
        private String baseTime;

        @JsonProperty("nx")
        private String nx;

        @JsonProperty("ny")
        private String ny;

        @JsonProperty("category")
        private String category;

        @JsonProperty("obsrValue")
        private String obsrValue;

        @JsonProperty("fcstDate")
        private String fcstDate;

        @JsonProperty("fcstValue")
        private String fcstValue;

        @JsonProperty("fcstTime")
        private String fcstTime;
    }
}