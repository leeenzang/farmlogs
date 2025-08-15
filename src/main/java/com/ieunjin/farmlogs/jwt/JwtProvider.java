// 토큰 발급, 파싱, 유효성 검증
package com.ieunjin.farmlogs.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    private final UserDetailsService userDetailsService;

    private final Key signingKey;

    // 생성자가 시크릿키를 Base64로 인코딩해서 JWT 서명에 쓸 수 있게 key로 변환
    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token.expire-time}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token.expire-time}") long refreshTokenExpireTime,
            UserDetailsService userDetailsService
    ) {
        this.secretKey = secretKey;
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
        this.userDetailsService = userDetailsService;
        this.signingKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
    }

    // accessToken 생성 username, role을 payload에 포함
    public String createAccessToken(String username, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // refreshToken 생성 내용 없이 시간만 포함
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 username 꺼내는 메서드
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true; // 파싱 성공 -> 유효함
        } catch (Exception e) {
            return false;  // 실패 -> 만료,위조,손상
        }
    }

    // 리프레시 토큰 남은 시간 조회
    public long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_EXPIRE_TIME;
    }

}