package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.dto.GenerationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerationService {

    public CarouselData generateCarousel(GenerationRequest request) {
        // Placeholder for Gemini API call
        // We will simulate a response here for now as requested by the plan.
        
        return new CarouselData(
            List.of(
                new CarouselData.Slide("Title 1", "Content 1 for " + request.topic(), "code 1"),
                new CarouselData.Slide("Title 2", "Content 2", "code 2"),
                new CarouselData.Slide("Title 3", "Content 3", "code 3"),
                new CarouselData.Slide("Title 4", "Content 4", "code 4"),
                new CarouselData.Slide("Title 5", "Content 5", "code 5"),
                new CarouselData.Slide("Title 6", "Content 6", "code 6"),
                new CarouselData.Slide("Title 7", "Content 7", "code 7")
            ),
            "Generated caption for " + request.topic(),
            List.of("#" + request.pillar().replaceAll("\\s", ""), "#backend")
        );
    }
}
