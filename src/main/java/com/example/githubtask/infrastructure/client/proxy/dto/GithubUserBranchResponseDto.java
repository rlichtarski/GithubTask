package com.example.githubtask.infrastructure.client.proxy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubUserBranchResponseDto(
        String name,
        CommitDto commit
) {
    public record CommitDto(
            String sha
    ) {

    }
}
