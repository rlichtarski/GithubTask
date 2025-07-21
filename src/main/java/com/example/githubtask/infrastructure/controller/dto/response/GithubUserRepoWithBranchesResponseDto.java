package com.example.githubtask.infrastructure.controller.dto.response;

import java.util.List;

public record GithubUserRepoWithBranchesResponseDto(
        String repositoryName,
        String ownerLogin,
        List<BranchDto> branches
) {
    public record BranchDto(
            String name,
            String lastCommitSha
    ) {
    }
}