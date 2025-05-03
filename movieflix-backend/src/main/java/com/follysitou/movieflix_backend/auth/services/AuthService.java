package com.follysitou.movieflix_backend.auth.services;

import com.follysitou.movieflix_backend.auth.entities.UserRole;
import com.follysitou.movieflix_backend.auth.entities.User;
import com.follysitou.movieflix_backend.auth.repositories.UserRepository;
import com.follysitou.movieflix_backend.auth.utils.AuthResponse;
import com.follysitou.movieflix_backend.auth.utils.LoginRequest;
import com.follysitou.movieflix_backend.auth.utils.RegisterRequest;
import com.follysitou.movieflix_backend.exceptions.ResourceAlreadyExistsException;
import com.follysitou.movieflix_backend.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {

        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException("Username already taken by another user !");
                });


        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {

            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with this email. Please, provide a correct email."));

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );

            } catch (BadCredentialsException ex) {
                    throw  new com.follysitou.movieflix_backend.exceptions.BadCredentialsException("Email and/or password incorrect.");
            }

            var accessToken = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());


            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .name(user.getName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();

    }
}
