package com.example.demo.utils;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadUtil {

    String uploadImage(MultipartFile file);

    String updateImage(MultipartFile file , String existingImageName);


    void deleteImage( String savedImageName);
}
