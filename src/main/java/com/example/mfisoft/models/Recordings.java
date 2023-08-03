package com.example.mfisoft.models;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

public record Recordings(
        Long id,
        String title,
        String url,
        String imageUrl,
        String newsSite,
        String summary,
        Instant publishedAt,
        Instant updatedAt,
        Boolean featured,
        List<Launches> launches,
        List<Events> events

) {
}
