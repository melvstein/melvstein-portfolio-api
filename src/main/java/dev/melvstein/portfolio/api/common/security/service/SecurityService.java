package dev.melvstein.portfolio.api.common.security.service;

import dev.melvstein.portfolio.api.common.config.AppProperties;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final AppProperties appProperties;

    public SecurityService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public boolean verifyApiKey(String apiKey) {
        return !appProperties.apiKey().equals(apiKey);
    }
}
