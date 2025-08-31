package com.ieunjin.farmlogs.dto.diary;

import com.ieunjin.farmlogs.entity.Diary;

import java.time.LocalDate;

public record DiaryListItemResponse(
        Long id,
        String title,
        LocalDate date,
        String weather
) {
    public static DiaryListItemResponse from(Diary diary) {
        return new DiaryListItemResponse(
                diary.getId(),
                String.format("%d년 %d월 %d일의 일기입니다.",
                        diary.getDate().getYear(),
                        diary.getDate().getMonthValue(),
                        diary.getDate().getDayOfMonth()),
                diary.getDate(),
                diary.getWeather()
        );
    }
}