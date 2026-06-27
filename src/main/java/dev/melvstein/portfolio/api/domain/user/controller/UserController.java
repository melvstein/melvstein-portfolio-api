package dev.melvstein.portfolio.api.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.domain.base.controller.BaseController;
import dev.melvstein.portfolio.api.domain.user.repository.specification.filter.UserFilter;
import dev.melvstein.portfolio.api.domain.user.service.UserService;
import dev.melvstein.portfolio.api.domain.user.vo.UserResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseVo.GetAll> getAllUsers(
            UserFilter filter,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getAllUsers(filter, pageable));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseVo.GetOne> getUserById(
            @PathVariable Long userId
    ) throws JsonProcessingException {
        return handleResponse(userId, userService::getUserById, UserResponseVo.GetOne::error);
    }
}
