package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.auth.LoginRequestDto;
import com.ieunjin.farmlogs.dto.auth.LoginResponseDto;
import com.ieunjin.farmlogs.dto.auth.RegisterRequestDto;
import com.ieunjin.farmlogs.entity.RefreshToken;
import com.ieunjin.farmlogs.entity.Role;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.jwt.JwtProvider;
import com.ieunjin.farmlogs.repository.RefreshTokenRepository;
import com.ieunjin.farmlogs.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void register(RegisterRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String password2 = requestDto.getPassword2();
        String nickname = requestDto.getNickname();

        // 아이디 중복 체크
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }

        // 비밀번호 확인
        if (!password.equals(password2)) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 유저 생성 및 저장
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String rawPassword = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElse(null);

        String passwordHash = (user != null) ? user.getPassword()
                : "$2a$10$7EqJtq98hPqEX7fNZaFWoOa5r5rQ9r5rQ9r5rQ9r5rQ9r";

        if (user == null || !passwordEncoder.matches(rawPassword, passwordHash)) {
            throw new BusinessException(ErrorCode.AUTH_FAILED);
        }

        // JWT 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken();

        // RefreshToken을 DB 저장
        RefreshToken token = RefreshToken.builder()
                .username(user.getUsername())
                .token(refreshToken)
                .expiration(LocalDateTime.now().plus(Duration.ofMillis(jwtProvider.getRefreshTokenExpiration())))
                .build();

        refreshTokenRepository.save(token);

        // 응답 DTO 구성
        return new LoginResponseDto(accessToken, refreshToken, user.getId(), user.getNickname());
    }

    @Transactional
    @Override
    public void logout(String token) {
        if (token == null || !jwtProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtProvider.getUsername(token);
        refreshTokenRepository.deleteByUsername(username);
    }
}