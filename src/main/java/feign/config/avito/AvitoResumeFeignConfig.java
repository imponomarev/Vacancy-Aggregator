package feign.config.avito;

import com.example.vacancy_aggregator.auth.avito.resume.AvitoTokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class AvitoResumeFeignConfig {
    @Bean
    public RequestInterceptor avitoAuthInterceptor(AvitoTokenService tokenService) {
        return template -> {
            String headerValue = "Bearer " + tokenService.token();
            template.header("Authorization", headerValue);
        };
    }
}
