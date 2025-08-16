package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.auth.LoginRequestDto;
import com.ieunjin.farmlogs.dto.auth.LoginResponseDto;
import com.ieunjin.farmlogs.dto.auth.RegisterRequestDto;
import com.ieunjin.farmlogs.jwt.JwtProvider;
import com.ieunjin.farmlogs.jwt.JwtUtils;
import com.ieunjin.farmlogs.repository.RefreshTokenRepository;
import com.ieunjin.farmlogs.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        log.info("회원가입 요청 username={}", requestDto.getUsername());
        authService.register(requestDto);
        log.info("회원가입 성공 username={}", requestDto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        log.info("로그인 요청 username={}", requestDto.getUsername());
        LoginResponseDto responseDto = authService.login(requestDto);
        log.info("로그인 성공 userid={}", responseDto.getUserId());
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = JwtUtils.resolveToken(request);
        String username = jwtProvider.getUsername(token);
        log.info("로그아웃 요청 username={}", username);
        authService.logout(token);
        log.info("로그아웃 성공 username={}", username);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}