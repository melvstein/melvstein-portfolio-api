package dev.melvstein.portfolio.api.domain.user.service;

import dev.melvstein.portfolio.api.domain.base.service.BaseService;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;

    UserService(
            UserRepository userRepository,
            @Value("${app.api-key}") String apiKey
    ) {
        this.userRepository = userRepository;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
       //System.out.println("API_KEY " + apiKey);
    }
}
