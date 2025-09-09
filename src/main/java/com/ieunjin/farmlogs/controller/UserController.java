package com.ieunjin.farmlogs.controller;

import com.ieunjin.farmlogs.dto.user.UserResponse;
import com.ieunjin.farmlogs.dto.user.UserUpdateRequest;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.repository.UserRepository;
import com.ieunjin.farmlogs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("회원 정보 조회 요청 - username={}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        return ResponseEntity.ok(userService.getUserInfo(user.getId()));
    }

    @Operation(summary = "회원 정보 수정")
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(@Valid @RequestBody UserUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("회원 정보 수정 요청 - username={}, nickname={}", username, request.getNickname());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        return ResponseEntity.ok(userService.updateUserInfo(user.getId(), request));
    }
}