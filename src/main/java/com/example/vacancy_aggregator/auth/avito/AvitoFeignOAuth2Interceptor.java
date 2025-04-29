package com.example.vacancy_aggregator.auth.avito;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class AvitoFeignOAuth2Interceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager authClientManager;

    public AvitoFeignOAuth2Interceptor(OAuth2AuthorizedClientManager authClientManager) {
        this.authClientManager = authClientManager;
    }

    @Override
    public void apply(RequestTemplate template) {

        String url = template.url();
        if (!(url.startsWith("/job/v2/vacancies") || url.startsWith("/job/v2/vacancies/"))) {
            return;  // пропустим HH и SuperJob
        }

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("avito")
                .principal(principal)
                .build();

        OAuth2AuthorizedClient client = authClientManager.authorize(authorizeRequest);
        if (client == null) {
            return;
        }

        String token = client.getAccessToken().getTokenValue();

        template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
