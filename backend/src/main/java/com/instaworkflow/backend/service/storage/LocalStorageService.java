package com.instaworkflow.backend.service.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@ConditionalOnProperty(name = "app.render.use-cloudinary", havingValue = "false", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    @Override
    public String saveImage(byte[] imageBytes, String fileName) throws Exception {
        Path outputDir = Paths.get("output", "build");
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
        Path outputPath = outputDir.resolve(fileName + ".png");
        Files.write(outputPath, imageBytes);
        return outputPath.toAbsolutePath().toString();
    }
}
