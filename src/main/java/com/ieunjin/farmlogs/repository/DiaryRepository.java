package com.ieunjin.farmlogs.repository;

import com.ieunjin.farmlogs.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {


}