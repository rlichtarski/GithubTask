package com.example.githubtask.infrastructure.client.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubClientErrorResponseDto(String message, String status) { }
