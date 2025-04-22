package com.follysitou.movieflix_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record MailBody(
        String to,
        String subject,
        String text
) {
}
