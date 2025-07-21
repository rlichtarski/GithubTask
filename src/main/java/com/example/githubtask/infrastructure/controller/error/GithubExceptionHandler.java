package com.example.githubtask.infrastructure.controller.error;

import com.example.githubtask.domain.model.GithubUserNotFoundException;
import com.example.githubtask.infrastructure.controller.GithubRestController;
import com.example.githubtask.infrastructure.controller.dto.response.GithubUserNotFoundResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = GithubRestController.class)
@Log4j2
public class GithubExceptionHandler {

    @ExceptionHandler(GithubUserNotFoundException.class)
    public ResponseEntity<GithubUserNotFoundResponseDto> handleGithubUserNotFoundException(GithubUserNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new GithubUserNotFoundResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

}
