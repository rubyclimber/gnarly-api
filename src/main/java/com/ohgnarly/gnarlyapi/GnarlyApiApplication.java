package com.ohgnarly.gnarlyapi;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

@SpringBootApplication
public class GnarlyApiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GnarlyApiApplication.class);
        String port = isNotBlank(getenv("PORT"))
                ? getenv("PORT")
                : "1966";
        app.setDefaultProperties(Collections.singletonMap("server.port", port));
        app.run(args);
    }

}
