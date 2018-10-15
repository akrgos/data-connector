package main.java.com.data.connector.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "sap-configuration")
public class SapConfiguration extends BaseConfiguration {
    private String systemNumber;
    private String client;
    private String function;
    private String destinationName;
}