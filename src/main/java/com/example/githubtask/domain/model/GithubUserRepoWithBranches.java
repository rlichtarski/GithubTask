package com.example.githubtask.domain.model;

import java.util.List;

public record GithubUserRepoWithBranches(
        String repoName,
        String ownerLogin,
        List<Branch> branches
) {
    public record Branch(
            String name,
            String lastCommitSha
    ) {
    }
}
