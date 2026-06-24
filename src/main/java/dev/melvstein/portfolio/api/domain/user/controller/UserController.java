package dev.melvstein.portfolio.api.domain.user.controller;

import dev.melvstein.portfolio.api.domain.base.controller.BaseController;
import dev.melvstein.portfolio.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;


}
