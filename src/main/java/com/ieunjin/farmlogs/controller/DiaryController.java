package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryListResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;
import com.ieunjin.farmlogs.jwt.JwtUtils;
import com.ieunjin.farmlogs.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "다이어리 등록")
    @PostMapping
    public ResponseEntity<DiaryResponse> createDiary(@RequestBody DiaryCreateRequest request) {
        DiaryResponse response = diaryService.createDiary(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 개별 조회")
    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponse> getDiary(@PathVariable Long id) {
        DiaryResponse response = diaryService.getDiaryById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 리스트 조회")
    @GetMapping
    public ResponseEntity<DiaryListResponse> getDiaryList(Pageable pageable) {
        DiaryListResponse response = diaryService.getDiaryList(pageable);
        return ResponseEntity.ok(response);
    }
}