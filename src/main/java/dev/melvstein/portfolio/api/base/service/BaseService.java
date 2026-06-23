package dev.melvstein.portfolio.api.base.service;

public abstract class BaseService {

    protected String apiKey;

    public boolean verifyApiKey(String requestApiKey) {
        return apiKey.equals(requestApiKey);
    }
}
