package com.ieunjin.farmlogs.dto.diary;

public record DiaryWithPastResponse(
        DiaryResponse today,
        DiaryResponse lastYear,
        DiaryResponse twoYearsAgo
) {}