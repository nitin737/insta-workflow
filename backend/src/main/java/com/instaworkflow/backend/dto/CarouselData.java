package com.instaworkflow.backend.dto;

import java.util.List;

public record CarouselData(
    List<Slide> slides,
    String caption,
    List<String> hashtags
) {
    public record Slide(
        String title,
        String content,
        String code
    ) {}
}
