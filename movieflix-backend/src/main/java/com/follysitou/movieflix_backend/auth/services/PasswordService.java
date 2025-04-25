package com.follysitou.movieflix_backend.auth.services;

import com.follysitou.movieflix_backend.auth.entities.User;
import com.follysitou.movieflix_backend.auth.repositories.UserRepository;
import com.follysitou.movieflix_backend.exceptions.BadCredentialsException;
import com.follysitou.movieflix_backend.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(String email, String oldPassword, String newPassword, String confirmNewPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email."));

        // Vérifier si l'ancien mot de passe est correct
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("The current password is incorrect.");
        }
        // Vérifier si le nouveau mot de passe et la confirmation correspondent
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New password and confirmation do not match.");
        }

        // Vérifier la force du mot de passe (par exemple, longueur minimale)
        /*if (newPassword.length() < 8) {
            throw new IllegalArgumentException("The new password must be at least 8 characters long.");
        }*/

        // Encoder le nouveau mot de passe
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(email, encodedNewPassword);
    }
}
