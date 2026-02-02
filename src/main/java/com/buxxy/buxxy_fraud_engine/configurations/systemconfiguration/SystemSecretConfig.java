package com.buxxy.buxxy_fraud_engine.configurations.systemconfiguration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix ="system" )
@Getter
public class SystemSecretConfig {
    private List<String> apiKey;
}
