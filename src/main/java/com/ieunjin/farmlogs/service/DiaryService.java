package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryListResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;

import org.springframework.data.domain.Pageable;

public interface DiaryService {
    DiaryResponse createDiary(DiaryCreateRequest request);
    DiaryResponse getDiaryById(Long id);
    DiaryListResponse getDiaryList(Pageable pageable);


}
