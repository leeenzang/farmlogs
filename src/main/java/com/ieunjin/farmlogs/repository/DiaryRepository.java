package com.ieunjin.farmlogs.repository;

import com.ieunjin.farmlogs.entity.Diary;
import com.ieunjin.farmlogs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findAllByUser(User user, Pageable pageable);

    Page<Diary> findAllByUserAndDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    Page<Diary> findByUserAndContentContainingIgnoreCase(
            User user,
            String keyword,
            Pageable pageable
    );

    List<Diary> findAllByUser(User user, Sort sort);

    List<Diary> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate, Sort sort);

}