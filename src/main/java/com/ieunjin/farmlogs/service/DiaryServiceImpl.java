package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.common.LunarDateUtil;
import com.ieunjin.farmlogs.dto.diary.DiaryCreateRequest;
import com.ieunjin.farmlogs.dto.diary.DiaryResponse;
import com.ieunjin.farmlogs.entity.Diary;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.jwt.JwtProvider;
import com.ieunjin.farmlogs.repository.DiaryRepository;
import com.ieunjin.farmlogs.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public DiaryResponse createDiary(String token, DiaryCreateRequest request) {
        // 토큰에서 유저명 파싱, 유저조회
        String username = jwtProvider.getUsername(token);
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
}