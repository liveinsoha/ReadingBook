package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("local")
@Configuration
public class LocalResourceConfig implements WebMvcConfigurer {



    private String imageResourcePath = "file:///"+ PathConst.IMAGE_SAVE_PATH;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(PathConst.IMAGE_PATH)
//                .addResourceLocations(imageResourcePath);
//    }

}