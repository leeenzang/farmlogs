package com.ieunjin.farmlogs.entity;

import com.ieunjin.farmlogs.common.LunarDateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate date;

    private String lunarDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String weather;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Diary(User user, LocalDate date, String lunarDate, String content, String weather) {
        this.user = user;
        this.date = date;
        this.lunarDate = lunarDate;
        this.content = content;
        this.weather = weather;
    }

    public void update(LocalDate date, String content, String weather) {
        this.date = date;
        this.content = content;
        this.weather = weather;
        this.lunarDate = LunarDateUtil.calculateLunarDate(date);
    }
}