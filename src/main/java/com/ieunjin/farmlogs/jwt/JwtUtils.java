// 요청 헤더에서 토큰 추출
package com.ieunjin.farmlogs.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {
    public static String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}