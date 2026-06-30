package com.instaworkflow.backend.service;

import com.instaworkflow.backend.dto.CarouselData;
import com.instaworkflow.backend.dto.GenerationRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GenerationService {

    private final ChatModel chatModel;

    @Value("classpath:gemini-carousel-prompt-1.md")
    private Resource promptResource;

    public GenerationService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public CarouselData generateCarouselJSON(GenerationRequest request) {
        BeanOutputConverter<CarouselData> converter = new BeanOutputConverter<>(CarouselData.class);

        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        Prompt prompt = promptTemplate.create(Map.of(
                "topic", request.topic()));

        String response = chatModel.call(prompt).getResult().getOutput().getText();
        return converter.convert(response);
    }
}
