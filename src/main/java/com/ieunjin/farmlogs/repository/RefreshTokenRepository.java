package com.ieunjin.farmlogs.repository;

import com.ieunjin.farmlogs.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUsername(String username);
}