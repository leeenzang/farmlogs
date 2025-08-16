package com.ieunjin.farmlogs.service;

import com.ieunjin.farmlogs.dto.auth.LoginRequestDto;
import com.ieunjin.farmlogs.dto.auth.LoginResponseDto;
import com.ieunjin.farmlogs.dto.auth.RegisterRequestDto;
import com.ieunjin.farmlogs.entity.Role;
import com.ieunjin.farmlogs.entity.User;
import com.ieunjin.farmlogs.exception.BusinessException;
import com.ieunjin.farmlogs.exception.ErrorCode;
import com.ieunjin.farmlogs.jwt.JwtProvider;
import com.ieunjin.farmlogs.repository.RefreshTokenRepository;
import com.ieunjin.farmlogs.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private AuthServiceImpl authService;

    public AuthServiceImplTest() {
        MockitoAnnotations.openMocks(this); // @Mock 초기화
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("tester");
        dto.setPassword("password123!");
        dto.setPassword2("password123!");
        dto.setNickname("nickname");

        given(userRepository.existsByUsername("tester")).willReturn(false);

        // when
        authService.register(dto);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void register_fail_duplicateUsername() {
        // given
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("tester");
        dto.setPassword("1234");
        dto.setPassword2("1234");
        dto.setNickname("nick");

        given(userRepository.existsByUsername("tester")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.DUPLICATE_USERNAME.name());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    void register_fail_passwordMismatch() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("tester");
        dto.setPassword("1234");
        dto.setPassword2("4321");

        given(userRepository.existsByUsername("tester")).willReturn(false);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.PASSWORD_MISMATCH.name());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        String rawPassword = "password123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .id(1L)
                .username("tester")
                .password(encodedPassword)
                .nickname("nick")
                .role(Role.USER)
                .build();

        given(userRepository.findByUsername("tester")).willReturn(Optional.of(user));
        given(jwtProvider.createAccessToken(any(), any())).willReturn("access-token");
        given(jwtProvider.createRefreshToken()).willReturn("refresh-token");
        given(jwtProvider.getRefreshTokenExpiration()).willReturn(3600000L); // 1시간

        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("tester");
        dto.setPassword(rawPassword);

        // when
        LoginResponseDto response = authService.login(dto);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrongPassword() {
        // given
        String encodedPassword = passwordEncoder.encode("password123!");
        User user = User.builder().username("tester").password(encodedPassword).role(Role.USER).build();

        given(userRepository.findByUsername("tester")).willReturn(Optional.of(user));

        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("tester");
        dto.setPassword("wrongpass");

        // when & then
        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.AUTH_FAILED.name());
    }
}