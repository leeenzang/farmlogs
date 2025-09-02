package com.ieunjin.farmlogs.dto.diary;

import com.ieunjin.farmlogs.entity.Diary;
import org.springframework.data.domain.Page;

import java.util.List;

public record DiaryListResponse(
        List<DiaryListItemResponse> content,
        PageInfo pageInfo
) {
    public static DiaryListResponse from(Page<Diary> page) {
        List<DiaryListItemResponse> content = page.getContent().stream()
                .map(DiaryListItemResponse::from)
                .toList();

        PageInfo pageInfo = new PageInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return new DiaryListResponse(content, pageInfo);
    }

    public record PageInfo(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean isLast
    ) {}
}