package com.ieunjin.farmlogs.dto.diary;

import java.time.LocalDate;

public record DiaryUpdateRequest(
        LocalDate date,
        String content,
        String weather
) {}