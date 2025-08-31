package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;

public interface DiaryService {
    DiaryResponse createDiary(String email, DiaryCreateRequest request);
}
