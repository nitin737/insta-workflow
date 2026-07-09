package com.instaworkflow.api.service.storage;

public interface StorageService {
    String saveImage(byte[] imageBytes, String fileName) throws Exception;
}
