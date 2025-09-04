package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryListResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;

import com.ieunjin.farmlogs.dto.diary.DiaryUpdateRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DiaryService {
    DiaryResponse createDiary(DiaryCreateRequest request);
    DiaryResponse getDiaryById(Long id);
    DiaryListResponse getDiaryList(LocalDate startDate, LocalDate endDate, String keyword, Pageable pageable);
    DiaryResponse updateDiary(Long diaryId, DiaryUpdateRequest request);
    void deleteDiary(Long diaryId);
    void exportDiariesToExcel(String username, LocalDate startDate, LocalDate endDate, HttpServletResponse response);
    DiaryResponse getDiaryByExactDate(LocalDate date);
}
