package com.instaworkflow.api.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.render.use-cloudinary", havingValue = "true")
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    public CloudinaryStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String saveImage(byte[] imageBytes, String fileName) throws Exception {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap("public_id", fileName));
        return (String) uploadResult.get("secure_url");
    }
}
