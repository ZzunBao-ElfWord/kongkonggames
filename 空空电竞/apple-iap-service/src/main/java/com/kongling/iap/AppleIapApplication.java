package com.kongling.iap;

import com.kongling.iap.config.AppleIapProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppleIapProperties.class)
public class AppleIapApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppleIapApplication.class, args);
    }
}
