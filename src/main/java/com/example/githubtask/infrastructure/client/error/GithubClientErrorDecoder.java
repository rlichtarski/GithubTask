package com.example.githubtask.infrastructure.client.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class GithubClientErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String s, Response response) {
        try {
            if (response != null && response.body() != null) {
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
