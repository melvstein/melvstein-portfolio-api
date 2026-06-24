package dev.melvstein.portfolio.api.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public boolean verifyApiKey(String apiKey, String requestApiKey) {
        return !apiKey.equals(requestApiKey);
    }
}
