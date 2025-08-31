package com.ieunjin.farmlogs.dto.diary;

import com.ieunjin.farmlogs.entity.Diary;

import java.time.LocalDate;
public record DiaryResponse(
        Long id,
        LocalDate date,
        String lunarDate,
        String content,
        String weather,
        String title
) {
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getId(),
                diary.getDate(),
                diary.getLunarDate(),
                diary.getContent(),
                diary.getWeather(),
                String.format("%d년 %d월 %d일의 일기입니다.",
                        diary.getDate().getYear(),
                        diary.getDate().getMonthValue(),
                        diary.getDate().getDayOfMonth()
                )
        );
    }
}