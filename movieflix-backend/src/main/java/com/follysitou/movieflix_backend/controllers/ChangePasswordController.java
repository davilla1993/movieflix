package com.follysitou.movieflix_backend.controllers;

import com.follysitou.movieflix_backend.auth.services.PasswordService;
import com.follysitou.movieflix_backend.dto.ChangePasswordRequest;
import com.follysitou.movieflix_backend.exceptions.BadCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/changePassword")
@RequiredArgsConstructor
public class ChangePasswordController {

    private final PasswordService passwordService;

    @PostMapping
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Récupérer l'email de l'utilisateur authentifié
            String email = userDetails.getUsername();

            passwordService.changePassword(email, changePasswordRequest.oldPassword(),
                    changePasswordRequest.newPassword(), changePasswordRequest.confirmNewPassword());

            return ResponseEntity.ok("Password changed successfully.");

        } catch (BadCredentialsException | IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
