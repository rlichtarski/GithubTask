package com.example.githubtask.infrastructure.client.proxy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubUserRepoResponseDto(
        String name,
        OwnerDto owner
) {
    public record OwnerDto(
            String login
    ) {

    }
}

