package com.instaworkflow.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;
import java.util.List;

@Builder
public record CarouselData(
        @JsonPropertyDescription("Name of the library or topic") String topic,
        @JsonProperty("slide1") Slide1 slide1,
        @JsonProperty("slide2") Slide2 slide2,
        @JsonProperty("slide3") Slide3 slide3,
        @JsonProperty("slide4") Slide4 slide4,
        @JsonProperty("slide5") Slide5 slide5) {
    public record Slide1(
            @JsonPropertyDescription("owner-username") String owner,
            @JsonPropertyDescription("repository-name") String repo,
            @JsonPropertyDescription("Star count, e.g. 39k") String stars,
            @JsonPropertyDescription("Repository display name") String bigTitle,
            @JsonPropertyDescription("Go (golang)") String highlightedText,
            @JsonPropertyDescription("Short repository subtitle or description including Go/golang(keep it short)") String description,
            @JsonPropertyDescription("Longer description of what the library does and who uses it.") String about,
            @JsonPropertyDescription("e.g. ['go', 'golang', 'cli', '...']") List<String> tags,
            @JsonPropertyDescription("e.g. Apache-2.0") String license,
            @JsonPropertyDescription("e.g. Active") String activity,
            @JsonPropertyDescription("e.g. 580") int watchers,
            @JsonPropertyDescription("e.g. 2900") int forks,
            @JsonPropertyDescription("e.g. v1.x.x Latest") String latestRelease,
            @JsonPropertyDescription("e.g. 280") int contributorsCount,
            @JsonPropertyDescription("e.g. 98.7") double langGoPct,
            @JsonPropertyDescription("e.g. 1.3") double langOtherPct,
            @JsonPropertyDescription("e.g. slide1-bg.jpg") String backgroundImage) {
    }

    public record Slide2(
            @JsonPropertyDescription("Headline for pain and cure slide, e.g. Solve CLI Boilerplate with Zero Friction.") String headline,
            @JsonPropertyDescription("Identify a common 'pain' developers face without this tool.") String pain,
            @JsonPropertyDescription("Provide a concise 'cure' explaining how this tool solves it.") String cure,
            @JsonPropertyDescription("Exactly 3 key 'features' highlighting its benefits.") List<String> features,
            @JsonPropertyDescription("e.g. background.png") String backgroundImage) {
    }

    public record Slide3(
            @JsonPropertyDescription("Headline for before and after slide, e.g. Standard Library—But Smarter.") String headline,
            @JsonPropertyDescription("Demonstrate the heavy or manual way of doing things. Escape double quotes with \\\" and use \\n for newlines.") String beforeCode,
            @JsonPropertyDescription("Show the elegant solution using the library. Escape double quotes with \\\" and use \\n for newlines.") String afterCode,
            @JsonPropertyDescription("A takeaway sentence summarizing the impact.") String takeaway,
            @JsonPropertyDescription("e.g. background.png") String backgroundImage) {
    }

    public record Slide4(
            @JsonPropertyDescription("Headline for features deep dive, e.g. Designed for Real-World Go Projects.") String headline,
            @JsonPropertyDescription("Array of exactly 4 points explaining specific technical advantages or design philosophies.") List<Point> points,
            @JsonPropertyDescription("e.g. background.png") String backgroundImage) {
        public record Point(
                @JsonPropertyDescription("Short title for the point") String title,
                @JsonPropertyDescription("Short description explaining the point") String desc) {
        }
    }

    public record Slide5(
            @JsonPropertyDescription("Closing headline, e.g. Add This to Your Go.mod Today.") String headline,
            @JsonPropertyDescription("One-liner quickstart terminal command") String quickstart,
            @JsonPropertyDescription("Minimal Go code snippet showing integration. Escape double quotes with \\\" and use \\n for newlines.") String minimalSetup,
            @JsonPropertyDescription("Exactly 2 resources links (website/docs and GitHub).") List<Resource> resources,
            @JsonPropertyDescription("Exactly 4 call-to-actions with emoji icons.") List<Cta> ctas,
            @JsonPropertyDescription("e.g. background.png") String backgroundImage) {
        public record Resource(
                @JsonPropertyDescription("Label for the resource, e.g. Read the Docs") String label,
                @JsonPropertyDescription("URL for the resource") String url) {
        }

        public record Cta(
                @JsonPropertyDescription("Emoji icon, e.g. \uD83D\uDCBE") String icon,
                @JsonPropertyDescription("Text for the call-to-action") String text) {
        }
    }
}
