package com.example.githubtask.infrastructure.controller.dto.response;

public record GithubUserNotFoundResponseDto(
        int status,
        String message
) {
}
