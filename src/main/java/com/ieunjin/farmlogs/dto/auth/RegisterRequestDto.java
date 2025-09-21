package com.ieunjin.farmlogs.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])[a-z0-9]{4,20}$",
            message = "아이디는 영어 소문자와 숫자로 4~20자여야 합니다."
    )
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    private String password2;
    @NotBlank
    private String nickname;
}
