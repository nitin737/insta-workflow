package com.instaworkflow.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CarouselData(
    String topic,
    @JsonProperty("slide1") Slide1 slide1,
    @JsonProperty("slide2") Slide2 slide2,
    @JsonProperty("slide3") Slide3 slide3,
    @JsonProperty("slide4") Slide4 slide4,
    @JsonProperty("slide5") Slide5 slide5
) {
    public record Slide1(
        String owner,
        String repo,
        String stars,
        String bigTitle,
        String highlightedText,
        String description,
        String about,
        List<String> tags,
        String license,
        String activity,
        int watchers,
        int forks,
        String latestRelease,
        int contributorsCount,
        double langGoPct,
        double langOtherPct,
        String backgroundImage
    ) {}

    public record Slide2(
        String headline,
        String pain,
        String cure,
        List<String> features,
        String backgroundImage
    ) {}

    public record Slide3(
        String headline,
        String beforeCode,
        String afterCode,
        String takeaway,
        String backgroundImage
    ) {}

    public record Slide4(
        String headline,
        List<Point> points,
        String backgroundImage
    ) {
        public record Point(
            String title,
            String desc
        ) {}
    }

    public record Slide5(
        String headline,
        String quickstart,
        String minimalSetup,
        List<Resource> resources,
        List<Cta> ctas,
        String backgroundImage
    ) {
        public record Resource(
            String label,
            String url
        ) {}

        public record Cta(
            String icon,
            String text
        ) {}
    }
}
