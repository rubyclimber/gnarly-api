package com.ohgnarly.gnarlyapi.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app")
public class ApplicationProperties {
    private String databaseName;
    private String databaseHost;
    private int databasePort;
    private String databaseUser;
    private String databasePassword;
    private String socketUrl;
}
