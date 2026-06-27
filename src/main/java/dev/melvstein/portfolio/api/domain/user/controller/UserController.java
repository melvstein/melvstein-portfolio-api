package dev.melvstein.portfolio.api.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.domain.base.controller.BaseController;
import dev.melvstein.portfolio.api.domain.user.dto.UserUpdateRequestDto;
import dev.melvstein.portfolio.api.domain.user.repository.specification.filter.UserFilter;
import dev.melvstein.portfolio.api.domain.user.service.UserService;
import dev.melvstein.portfolio.api.domain.user.vo.UserResponseVo;
import jakarta.validation.Valid;
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

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseVo.Update> updateUserById(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDto request
    ) throws JsonProcessingException {
        return handleResponse(userId, request, userService::updateUserById, UserResponseVo.Update::error);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponseVo.Delete> deleteUserById(
            @PathVariable Long userId
    ) throws JsonProcessingException {
        return handleResponse(userId, userService::deleteUserById, UserResponseVo.Delete::error);
    }
}
