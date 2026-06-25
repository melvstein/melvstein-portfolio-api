package dev.melvstein.portfolio.api.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.melvstein.portfolio.api.common.config.AppProperties;
import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.common.redis.service.RedisService;
import dev.melvstein.portfolio.api.data.UserData;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthLoginRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final String REGISTER_PATH = "/api/v1/auth/register";

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private RedisService redisService;

    @Autowired
    private UserData userData;

    private User userDetails;
    private AuthRegisterRequestDto registerRequest;
    private AuthLoginRequestDto loginRequest;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userDetails = userData.get();
        registerRequest = UserData.registerRequest();
        loginRequest = UserData.loginRequest();
    }

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        String request = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(
                post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("X-API-Key", appProperties.apiKey())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        userRepository.save(userDetails);

        when(redisService.getCachedUser(userDetails.getUsername()))
                .thenReturn(Optional.empty());

        String request = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("X-API-Key", appProperties.apiKey())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }
}
