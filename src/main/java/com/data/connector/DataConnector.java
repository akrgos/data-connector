package com.data.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.data")
public class DataConnector {
    public static void main(String[] args) {
        SpringApplication.run(DataConnector.class, args);
    }
}