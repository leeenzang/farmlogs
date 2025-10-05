package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.common.LunarDateUtil;
import com.ieunjin.farmlogs.dto.diary.*;
import com.ieunjin.farmlogs.entity.Diary;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.jwt.JwtProvider;
import com.ieunjin.farmlogs.repository.DiaryRepository;
import com.ieunjin.farmlogs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public DiaryResponse createDiary(DiaryCreateRequest request) {
        // 토큰에서 유저명 파싱, 유저조회
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        // 음력 변환
        String lunarDate = LunarDateUtil.calculateLunarDate(request.date());

        Diary diary = new Diary(
                user,
                request.date(),
                lunarDate,
                request.content(),
                request.weather()
        );

        diaryRepository.save(diary);
        return DiaryResponse.from(diary);
    }


    @Transactional(readOnly = true)
    @Override
    public DiaryResponse getDiaryById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DIARY));

        if (!diary.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return DiaryResponse.from(diary);
    }

    @Transactional(readOnly = true)
    public DiaryListResponse getDiaryList(
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Pageable pageable
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "date")
        );
        Page<Diary> page;
        if (keyword != null && !keyword.isBlank()) {
            // 키워드 검색
            page = diaryRepository.findByUserAndContentContainingIgnoreCase(user, keyword, sortedPageable);
        } else if (startDate != null && endDate != null) {
            // 날짜 범위 검색
            page = diaryRepository.findAllByUserAndDateBetween(user, startDate, endDate, sortedPageable);
        } else {
            //전체 조회
            page = diaryRepository.findAllByUser(user, sortedPageable);
        }
        return DiaryListResponse.from(page);
    }

    @Transactional
    public DiaryResponse updateDiary(Long diaryId, DiaryUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DIARY));

        if (!diary.getUser().getUsername().equals(username)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        diary.updateContent(request.content());

        return DiaryResponse.from(diary);
    }

    @Transactional
    @Override
    public void deleteDiary(Long diaryId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DIARY));

        if (!diary.getUser().getUsername().equals(username)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        diaryRepository.delete(diary);
    }


    @Transactional(readOnly = true)
    public void exportDiariesToExcel(
            String username,
            LocalDate startDate,
            LocalDate endDate,
            HttpServletResponse response
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        List<Diary> diaries = diaryRepository.findAllByUserAndDateBetween(
                user, startDate, endDate, Sort.by(Sort.Direction.DESC, "date")
        );

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Diaries");

            // 헤더
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("날짜 (양력)");
            headerRow.createCell(1).setCellValue("날짜 (음력)");
            headerRow.createCell(2).setCellValue("내용");

            // 데이터
            int rowIdx = 1;
            for (Diary diary : diaries) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(diary.getDate().toString());
                row.createCell(1).setCellValue(diary.getLunarDate());
                row.createCell(2).setCellValue(diary.getContent());
            }

            String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            String filename = "logs_" + todayStr + ".xlsx";

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);

            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // 체크 예외를 런타임 예외로 감싸서 위로 올림
            throw new RuntimeException("엑셀 내보내기 중 오류 발생", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DiaryResponse getDiaryByExactDate(LocalDate date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Diary diary = diaryRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DIARY));

        return DiaryResponse.from(diary);
    }

    public DiaryWithPastResponse getDiaryWithPast(LocalDate date) {
        DiaryResponse today = getDiaryByExactDate(date);
        DiaryResponse lastYear = getDiaryByExactDate(date.minusYears(1));
        DiaryResponse twoYearsAgo = getDiaryByExactDate(date.minusYears(2));
        return new DiaryWithPastResponse(today, lastYear, twoYearsAgo);
    }

    public List<LocalDate> getDiaryDatesOfMonth(String username, int year, int month) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return diaryRepository.findAllByUserAndDateBetween(user, start, end)
                .stream()
                .map(Diary::getDate)
                .distinct()
                .toList();
    }
}