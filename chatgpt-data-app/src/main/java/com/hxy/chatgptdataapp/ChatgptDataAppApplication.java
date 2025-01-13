package com.hxy.chatgptdataapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan
@SpringBootApplication
@ComponentScan(basePackages = {"com.hxy.*"})
public class ChatgptDataAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatgptDataAppApplication.class, args);
    }

}
