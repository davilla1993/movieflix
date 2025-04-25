package com.follysitou.movieflix_backend.dto;

public record ResetPasswordRequest(
        String password,
        String repeatPassword
) {
}
