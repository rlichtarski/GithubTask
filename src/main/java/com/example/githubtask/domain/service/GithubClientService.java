package com.example.githubtask.domain.service;

import com.example.githubtask.domain.model.GithubUserRepoWithBranches;
import com.example.githubtask.domain.service.config.ExecutorConfig;
import com.example.githubtask.infrastructure.client.proxy.GithubClientProxy;
import com.example.githubtask.infrastructure.client.proxy.dto.GithubUserBranchResponseDto;
import com.example.githubtask.infrastructure.client.proxy.dto.GithubUserRepoResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class GithubClientService {

    private final GithubClientProxy githubClientProxy;
    private final ExecutorService executorService;

    public List<GithubUserRepoWithBranches> fetchUserRepoWithBranches(String username) {
        List<GithubUserRepoResponseDto> userRepos = githubClientProxy.getUserRepos(username)
                .stream()
                .filter(repo -> !repo.fork())
                .toList();

        return userRepos.stream()
                .map(repository -> CompletableFuture
                        .supplyAsync(() -> getRepoBranches(repository), executorService))
                .map(CompletableFuture::join)
                .toList();
    }

    private GithubUserRepoWithBranches getRepoBranches(GithubUserRepoResponseDto userRepo) {
        List<GithubUserBranchResponseDto> branchesDto = githubClientProxy.getUserRepoBranches(
                userRepo.owner().login(), userRepo.name()
        );
        List<GithubUserRepoWithBranches.Branch> branches = getUserBranches(branchesDto);

        return new GithubUserRepoWithBranches(
                userRepo.name(),
                userRepo.owner().login(),
                branches
        );
    }

    private List<GithubUserRepoWithBranches.Branch> getUserBranches(List<GithubUserBranchResponseDto> branchesDto) {
        return branchesDto.stream()
                .map(branch -> new GithubUserRepoWithBranches.Branch(
                        branch.name(),
                        branch.commit().sha()
                ))
                .toList();
    }

}
