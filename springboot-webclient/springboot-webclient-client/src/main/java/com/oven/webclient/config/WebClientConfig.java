package com.oven.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        // HttpClient httpClient = HttpClient.create()
        //         // .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 5s连接超时
        //         .responseTimeout(Duration.ofMinutes(3))
        //         .doOnConnected(conn ->
        //                 conn.addHandlerLast(new ReadTimeoutHandler(3, TimeUnit.MINUTES))
        //                         .addHandlerLast(new WriteTimeoutHandler(3, TimeUnit.MINUTES)));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector());
    }

}
