package com.example.githubtask.infrastructure.controller;

import com.example.githubtask.domain.model.GithubUserRepoWithBranches;
import com.example.githubtask.domain.service.GithubClientService;
import com.example.githubtask.infrastructure.controller.dto.response.GithubUserRepoWithBranchesResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
@Log4j2
@AllArgsConstructor
public class GithubRestController {

    private final GithubClientService githubClientService;

    @GetMapping(value = "/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GithubUserRepoWithBranchesResponseDto>> getRepositoriesByUserName(
            @Valid @RequestParam String userName
    ) {
        List<GithubUserRepoWithBranches> githubUserRepositoriesWithBranches = githubClientService
                .fetchUserRepoWithBranches(userName);
        List<GithubUserRepoWithBranchesResponseDto> response = GithubMapper
                .mapListGithubUserRepositoriesWithBranchesToDto(githubUserRepositoriesWithBranches);
        return ResponseEntity.ok(response);
    }

}

