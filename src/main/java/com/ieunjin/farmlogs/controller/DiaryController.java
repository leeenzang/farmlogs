package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.diary.*;
import com.ieunjin.farmlogs.jwt.JwtUtils;
import com.ieunjin.farmlogs.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<DiaryListResponse> getDiaryList(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        log.info("다이어리 목록 조회 요청 - page={}, size={}, startDate={}, endDate={}, keyword={}",
                pageable.getPageNumber(), pageable.getPageSize(), startDate, endDate, keyword);
        DiaryListResponse response = diaryService.getDiaryList(startDate, endDate, keyword, pageable);
        log.info("다이어리 목록 조회 성공");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 수정")
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

    @Operation(summary = "다이어리 내보내기")
    @GetMapping("/export")
    public void exportDiaries(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            HttpServletResponse response
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("다이어리 엑셀 내보내기 요청 - username={}, startDate={}, endDate={}",
                username, startDate, endDate);
        diaryService.exportDiariesToExcel(username, startDate, endDate, response);
        log.info("다이어리 엑셀 내보내기 완료 - username={}", username);
    }

    @Operation(summary = "특정 날짜 + 과거 다이어리 조회")
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getDiaryByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false, defaultValue = "false") boolean include_past
    ) {
        if (include_past) {
            log.info("특정 날짜 + 과거 일기 조회 요청 - date={}", date);
            DiaryWithPastResponse response = diaryService.getDiaryWithPast(date);
            return ResponseEntity.ok(response);
        } else {
            log.info("특정 날짜 다이어리 조회 요청 - date={}", date);
            DiaryResponse response = diaryService.getDiaryByExactDate(date);
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "특정 연월의 일기 날짜 목록 조회")
    @GetMapping("/calendar")
    public ResponseEntity<?> getDiaryDatesOfMonth(
            @RequestParam int year,
            @RequestParam int month
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<LocalDate> dates = diaryService.getDiaryDatesOfMonth(username, year, month);
        return ResponseEntity.ok(Map.of("dates", dates));
    }
}