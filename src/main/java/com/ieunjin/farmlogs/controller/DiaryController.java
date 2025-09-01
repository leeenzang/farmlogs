package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryListResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryUpdateRequest;
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
        log.info("다이어리 작성 요청 date={}", request.date());
        DiaryResponse response = diaryService.createDiary(request);
        log.info("다이어리 작성 성공 diaryId={}", response.id());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 개별 조회")
    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponse> getDiary(@PathVariable Long id) {
        log.info("다이어리 조회 요청 diaryId={}", id);
        DiaryResponse response = diaryService.getDiaryById(id);
        log.info("다이어리 조회 성공 diaryId={}", response.id());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 리스트 조회")
    @GetMapping
    public ResponseEntity<DiaryListResponse> getDiaryList(Pageable pageable) {
        log.info("다이어리 목록 조회 요청 page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        DiaryListResponse response = diaryService.getDiaryList(pageable);
        log.info("다이어리 목록 조회 성공");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryResponse> updateDiary(
            @PathVariable Long id,
            @RequestBody DiaryUpdateRequest request
    ) {
        log.info("다이어리 수정 요청 diaryId={}", id);
        DiaryResponse response = diaryService.updateDiary(id, request);
        log.info("다이어리 수정 성공 diaryId={}", response.id());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long id) {
        log.info("다이어리 삭제 요청 diaryId={}", id);
        diaryService.deleteDiary(id);
        log.info("다이어리 삭제 완료 diaryId={}", id);
        return ResponseEntity.noContent().build();
    }
}