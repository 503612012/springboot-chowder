package com.oven.minio.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIoConfig {

    @Bean
    public MinioClient initMinioClient() {
        return MinioClient.builder()
                .endpoint("http://192.168.1.69:9000")
                .credentials("AC3EAkuaiyGASwgP0DIl", "uQZiWKrD5SxsjUWJdF7Ed9s6fs5LvtCKyzPgKzUX")
                .build();
    }

}
