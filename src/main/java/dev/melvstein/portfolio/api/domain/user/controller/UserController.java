package dev.melvstein.portfolio.api.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.domain.base.controller.BaseController;
import dev.melvstein.portfolio.api.domain.user.dto.UserCreateRequestDto;
import dev.melvstein.portfolio.api.domain.user.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.domain.user.service.UserService;
import dev.melvstein.portfolio.api.domain.user.vo.UserResponseVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;


}
