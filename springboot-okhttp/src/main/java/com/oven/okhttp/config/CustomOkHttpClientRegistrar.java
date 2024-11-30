package com.oven.okhttp.config;

import com.github.lianjiatech.retrofit.spring.boot.core.SourceOkHttpClientRegistrar;
import com.github.lianjiatech.retrofit.spring.boot.core.SourceOkHttpClientRegistry;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class CustomOkHttpClientRegistrar implements SourceOkHttpClientRegistrar {

    @Override
    public void register(SourceOkHttpClientRegistry registry) {
        registry.register("OvenOkHttpClient", new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(180))
                .writeTimeout(Duration.ofSeconds(180))
                .readTimeout(Duration.ofSeconds(180))
                .addInterceptor(chain -> {
                    log.info("================== >>> Use OvenOkHttpClient <<<==================");
                    return chain.proceed(chain.request());
                })
                .build());
    }

}