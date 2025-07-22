package com.example.githubtask.infrastructure.client.error;

import com.example.githubtask.domain.model.GithubUserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class GithubClientErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response != null && response.body() != null) {
                if (response.status() == 404 && methodKey.contains("getUserRepos")) {
                    return new GithubUserNotFoundException("GitHub user was not found");
                }
                InputStream bodyStream = response.body().asInputStream();

                GithubClientErrorResponseDto errorResponse = objectMapper.readValue(bodyStream, GithubClientErrorResponseDto.class);

                String message = errorResponse.message() != null ? errorResponse.message() : "Unknown error";
                int status = response.status();

                throw new GithubApiException(status, message);
            }
        } catch (IOException exception) {
            throw new GithubApiException(response.status(), exception.getMessage());
        }
        return new GithubApiException(response.status(), "Unknown error");
    }

}
