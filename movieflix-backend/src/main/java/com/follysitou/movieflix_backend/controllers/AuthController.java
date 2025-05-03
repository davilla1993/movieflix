package com.follysitou.movieflix_backend.controllers;

import com.follysitou.movieflix_backend.auth.entities.RefreshToken;
import com.follysitou.movieflix_backend.auth.entities.User;
import com.follysitou.movieflix_backend.auth.services.AuthService;
import com.follysitou.movieflix_backend.auth.services.JwtService;
import com.follysitou.movieflix_backend.auth.services.RefreshTokenService;
import com.follysitou.movieflix_backend.auth.utils.AuthResponse;
import com.follysitou.movieflix_backend.auth.utils.LoginRequest;
import com.follysitou.movieflix_backend.auth.utils.RefreshTokenRequest;
import com.follysitou.movieflix_backend.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                        .refreshToken(refreshToken.getRefreshToken())
                        .build());
    }
}
