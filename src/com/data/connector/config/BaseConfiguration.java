package com.data.connector.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseConfiguration {
    private String host;
    private String user;
    private String password;
    private String language = "en";
    private String table = "default";
}