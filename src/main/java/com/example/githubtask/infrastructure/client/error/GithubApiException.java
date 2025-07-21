package com.example.githubtask.infrastructure.client.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubApiException extends RuntimeException {
    private final int status;
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
