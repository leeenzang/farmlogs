package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.auth.LoginRequestDto;
import com.ieunjin.farmlogs.dto.auth.LoginResponseDto;
import com.ieunjin.farmlogs.dto.auth.RegisterRequestDto;

public interface AuthService {

    void register(RegisterRequestDto requestDto);
    LoginResponseDto login(LoginRequestDto requestDto);

}