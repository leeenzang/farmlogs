package com.ieunjin.farmlogs.service;


import com.ieunjin.farmlogs.dto.user.UserResponse;
import com.ieunjin.farmlogs.dto.user.UserUpdateRequest;

public interface UserService {
    UserResponse getUserInfo(Long userId);
    UserResponse updateUserInfo(Long userId, UserUpdateRequest request);
}
