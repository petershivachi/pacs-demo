package com.shivachi.pacs.demo.config.asynchttp;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AsyncHttpClientBuilder {

    @Bean
    public AsyncHttpClient buildClient() {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(Duration.ofSeconds(120))
                .setRequestTimeout(Duration.ofSeconds(120))
                .setReadTimeout(Duration.ofSeconds(120));

        return Dsl.asyncHttpClient(clientBuilder);
    }
}
