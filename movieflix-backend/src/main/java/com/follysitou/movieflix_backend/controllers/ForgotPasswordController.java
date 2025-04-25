package com.follysitou.movieflix_backend.controllers;

import com.follysitou.movieflix_backend.auth.entities.ForgotPassword;
import com.follysitou.movieflix_backend.auth.entities.User;
import com.follysitou.movieflix_backend.auth.repositories.ForgotPasswordRepository;
import com.follysitou.movieflix_backend.auth.repositories.UserRepository;
import com.follysitou.movieflix_backend.dto.ResetPasswordRequest;
import com.follysitou.movieflix_backend.dto.MailBody;
import com.follysitou.movieflix_backend.exceptions.InvalidResourceException;
import com.follysitou.movieflix_backend.exceptions.ResourceNotFoundException;
import com.follysitou.movieflix_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ForgotPasswordRepository forgotPasswordRepository;

    @Value("${security.app.forgot-password.otp-expiration-seconds}")
    private long otpExpirationSeconds;


    // Send mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found. Please provide a valid email!"));

        Optional<ForgotPassword> existingOtp = forgotPasswordRepository.findByUser(user);

        // Si un OTP existe et n'est pas encore expiré
        if (existingOtp.isPresent() && existingOtp.get().getExpirationTime().after(new Date())) {
            return ResponseEntity.badRequest().body("An OTP is already active for this user. Please wait for it to expire.");
        }

        // Supprimer l'ancien OTP s'il existe (expiré)
        existingOtp.ifPresent(
                 fp -> {
                    fp.setUser(null);              // couper la liaison
                    user.setForgotPassword(null); // si utile
                    forgotPasswordRepository.delete(fp);
                    forgotPasswordRepository.flush(); // optionnel mais utile
                }
        );

        int otp = otpGenerator();

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your forgot password request: " + otp)
                .subject("OTP for forgot password request")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + otpExpirationSeconds * 1000))
                .user(user)
                .build();

        forgotPasswordRepository.save(fp);
        emailService.sendSimpleMessage(mailBody);


        return ResponseEntity.ok("OTP email sent successfully!");
    }


    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found. Please provide an valid email !"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new InvalidResourceException("Invalid OTP for email : " + email));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))) {
            fp.setUser(null);
            user.setForgotPassword(null);
            forgotPasswordRepository.delete(fp);
            forgotPasswordRepository.flush();

            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPasswordHandler(@RequestBody ResetPasswordRequest resetPasswordRequest,
                                                        @PathVariable String email) {

        if(!Objects.equals(resetPasswordRequest.password(), resetPasswordRequest.repeatPassword())) {
            return new ResponseEntity<>("Passwords do not match!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(resetPasswordRequest.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed successfully !");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
