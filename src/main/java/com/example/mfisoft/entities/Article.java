package com.example.mfisoft.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("articles")
public record Article(
        @Id
        Long id,
        String title,
        String newsSite,
        Instant publishedDate,
        byte[] article
) {
}
