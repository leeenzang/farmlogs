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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
        authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = JwtUtils.resolveToken(request);

        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String username = jwtProvider.getUsername(token);
        refreshTokenRepository.deleteByUsername(username);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}