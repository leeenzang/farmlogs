package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.common.LunarDateUtil;
import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryListResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;
import com.ieunjin.farmlogs.dto.diary.DiaryUpdateRequest;
import com.ieunjin.farmlogs.entity.Diary;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.jwt.JwtProvider;
import com.ieunjin.farmlogs.repository.DiaryRepository;
import com.ieunjin.farmlogs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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


    public DiaryResponse getDiaryById(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DIARY));
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
}