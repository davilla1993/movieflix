package com.follysitou.movieflix_backend.dto;

public record ChangePassword(
        String password,
        String repeatPassword
) {
}
