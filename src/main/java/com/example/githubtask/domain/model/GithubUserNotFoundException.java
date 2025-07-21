package com.example.githubtask.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubUserNotFoundException extends RuntimeException {
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}

