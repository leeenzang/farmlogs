package com.ieunjin.farmlogs.repository;

import com.ieunjin.farmlogs.entity.Diary;
import com.ieunjin.farmlogs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findAllByUser(User user, Pageable pageable);
}