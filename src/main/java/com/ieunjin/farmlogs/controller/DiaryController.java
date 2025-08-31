package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;
import com.ieunjin.farmlogs.jwt.JwtUtils;
import com.ieunjin.farmlogs.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "다이어리 등록")
    @PostMapping
    public ResponseEntity<DiaryResponse> createDiary(@RequestBody DiaryCreateRequest request,
                                                     HttpServletRequest httpServletRequest) {
        String token = JwtUtils.resolveToken(httpServletRequest);
        DiaryResponse response = diaryService.createDiary(token, request);

        return ResponseEntity.ok(response);
    }
}