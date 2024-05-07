package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("deploy")
@Configuration
public class DeployResourceConfig implements WebMvcConfigurer {

//    @Bean
//    public ImageUploadUtil imageUploadUtil(){
//       // return new AwsS3ImageUploadUtil(amazonS3Client());
//         return new ImageUploadUtilImpl();
//    }


    private String imageResourcePath = "";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PathConst.IMAGE_PATH)
                .addResourceLocations(imageResourcePath);
    }

}