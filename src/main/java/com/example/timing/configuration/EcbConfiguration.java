package com.example.timing.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Data
@Configuration
@ConfigurationProperties(prefix = "ecb")
public class EcbConfiguration {

        @NotBlank(message = "ecb.url should be set in the configuration")
        private String url;
}
