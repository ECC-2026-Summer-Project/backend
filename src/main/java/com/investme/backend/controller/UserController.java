package com.investme.backend.controller;

import com.investme.backend.dto.*;
import com.investme.backend.entity.User;
import com.investme.backend.jwt.JwtProvider;
import com.investme.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public UserController(
            UserService userService,
            JwtProvider jwtProvider
    ) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(
            @RequestBody SignupRequest request
    ) {

        User user = userService.signup(request);

        SignupResponse response =
                new SignupResponse(user.getUserId());

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @GetMapping("/check-id")
    public ApiResponse<CheckIdResponse> checkId(
            @RequestParam String userId
    ) {

        boolean available =
                userService.checkUserId(userId);

        CheckIdResponse response =
                new CheckIdResponse(available);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        User user = userService.login(request);

        String accessToken =
                jwtProvider.createToken(user.getUserId());

        LoginResponse response =
                new LoginResponse(
                        user.getUserId(),
                        accessToken,
                        user.getRefreshToken()
                );

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(

            @RequestParam String userId,

            @RequestBody PasswordChangeRequest request
    ) {

        userService.changePassword(
                userId,
                request
        );

        return new ApiResponse<>(
                true,
                null,
                null
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestParam String userId
    ){

        userService.logout(userId);

        return new ApiResponse<>(
                true,
                null,
                null
        );
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(
            @RequestParam String userId
    ){

        userService.deleteUser(userId);

        return new ApiResponse<>(
                true,
                null,
                null
        );
    }

}