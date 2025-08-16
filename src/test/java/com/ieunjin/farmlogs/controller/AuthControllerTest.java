package com.ieunjin.farmlogs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieunjin.farmlogs.dto.auth.LoginRequestDto;
import com.ieunjin.farmlogs.dto.auth.RegisterRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("tester1");
        dto.setPassword("password123!");
        dto.setPassword2("password123!");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        RegisterRequestDto regDto = new RegisterRequestDto();
        regDto.setUsername("tester2");
        regDto.setPassword("password123!");
        regDto.setPassword2("password123!");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regDto)));

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("tester2");
        loginDto.setPassword("password123!");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() throws Exception {
        RegisterRequestDto regDto = new RegisterRequestDto();
        regDto.setUsername("tester3");
        regDto.setPassword("password123!");
        regDto.setPassword2("password123!");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regDto)));

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("tester3");
        loginDto.setPassword("password123!");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 되었습니다."));
    }
}