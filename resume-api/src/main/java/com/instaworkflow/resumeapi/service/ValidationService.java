package com.instaworkflow.resumeapi.service;

import com.instaworkflow.resumeapi.exception.ValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    @Value("${resume.validation.schema-location:classpath:resume-schema.json}")
    private Resource schemaResource;

    private Schema schema;

    @PostConstruct
    public void init() throws IOException {
        try (InputStream inputStream = schemaResource.getInputStream()) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            this.schema = SchemaLoader.load(rawSchema);
        }
    }

    public void validate(JsonNode data) {
        try {
            JSONObject subject = new JSONObject(data.toString());
            schema.validate(subject);
        } catch (org.everit.json.schema.ValidationException e) {
            List<String> errors = e.getAllMessages();
            throw new ValidationException("Invalid resume data: " + String.join(", ", errors));
        }
    }
}
