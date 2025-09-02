package com.ieunjin.farmlogs.dto.diary;

import java.time.LocalDate;

public record DiaryCreateRequest(
        LocalDate date,
        String content,
        String weather
) {}