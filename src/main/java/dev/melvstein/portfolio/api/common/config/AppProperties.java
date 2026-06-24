package dev.melvstein.portfolio.api.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(

        String apiKey
) {
}
