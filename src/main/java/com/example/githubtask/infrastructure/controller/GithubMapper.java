package com.example.githubtask.infrastructure.controller;

import com.example.githubtask.domain.model.GithubUserRepoWithBranches;
import com.example.githubtask.infrastructure.controller.dto.response.GithubUserRepoWithBranchesResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class GithubMapper {

    public static List<GithubUserRepoWithBranchesResponseDto> mapListGithubUserRepositoriesWithBranchesToDto(
            List<GithubUserRepoWithBranches> domainGithubUserRepoWithBranches
    ) {
        return domainGithubUserRepoWithBranches.stream()
                .map(GithubMapper::mapGithubUserRepoWithBranchesToDto)
                .collect(Collectors.toList());
    }

    private static GithubUserRepoWithBranchesResponseDto mapGithubUserRepoWithBranchesToDto(
            GithubUserRepoWithBranches githubUserRepoWithBranches
    ) {
        List<GithubUserRepoWithBranchesResponseDto.BranchDto> branchesDto = getBranchesDto(githubUserRepoWithBranches);
        return new GithubUserRepoWithBranchesResponseDto(
                githubUserRepoWithBranches.repoName(),
                githubUserRepoWithBranches.ownerLogin(),
                branchesDto
        );
    }

    private static List<GithubUserRepoWithBranchesResponseDto.BranchDto> getBranchesDto(GithubUserRepoWithBranches githubUserRepoWithBranches) {
        return githubUserRepoWithBranches.branches()
                .stream()
                .map(branch -> new GithubUserRepoWithBranchesResponseDto.BranchDto(
                        branch.name(),
                        branch.lastCommitSha()
                ))
                .toList();
    }
}
