package com.example.demo;

import com.example.demo.web.util.InitAdminClass;
import com.example.demo.web.util.InitClass;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    InitClass initClass;

    @Autowired
    InitAdminClass initAdminClass;

    @PostConstruct
    void initData() {
        initClass.initData();
        initAdminClass.initData();
    }


}
