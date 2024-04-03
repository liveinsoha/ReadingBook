package com.example.demo.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.demo.utils.AwsS3ImageUploadUtil;
import com.example.demo.utils.ImageUploadUtil;
import com.example.demo.utils.ImageUploadUtilImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Bean
    public ImageUploadUtil imageUploadUtil(){
       // return new AwsS3ImageUploadUtil(amazonS3Client());
         return new ImageUploadUtilImpl();
    }


    private String imageResourcePath = "file:///"+ PathConst.IMAGE_SAVE_PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PathConst.IMAGE_PATH)
                .addResourceLocations(imageResourcePath);
    }
}