package com.example.vacancy_aggregator.auth.avito.vacancy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * Общая конфигурация OAuth2-клиента для Avito:
 * поддержка authorization_code + refresh_token.
 */
@Configuration
public class OAuth2ClientConfig {

    /**
     * Создает {@link OAuth2AuthorizedClientManager}, который умеет
     * выполнять Authorization Code flow и автоматически обновлять токены.
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clients,
            OAuth2AuthorizedClientService clientService) {

        var provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clients, clientService);

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
}
