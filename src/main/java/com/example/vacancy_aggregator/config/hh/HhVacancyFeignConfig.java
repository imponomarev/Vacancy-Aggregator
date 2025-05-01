package com.example.vacancy_aggregator.config.hh;

import feign.okhttp.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HhVacancyFeignConfig {

    @Bean
    public OkHttpClient feignOkHttpClient() {
        okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request modified = original.newBuilder()
                                .removeHeader("Authorization")
                                .build();
                        return chain.proceed(modified);
                    }
                })
                .build();

        return new OkHttpClient(okHttp3Client);
    }
}