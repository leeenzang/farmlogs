package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.user.UserResponse;
import com.ieunjin.farmlogs.dto.user.UserUpdateRequest;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return UserResponse.from(user);
    }

    @Override
    public UserResponse updateUserInfo(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        // 닉네임 변경
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            user.setNickname(request.getNickname());
        }

        //비번 변경
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }

        return UserResponse.from(user);
    }
}