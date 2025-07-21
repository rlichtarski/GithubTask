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

    public List<GithubUserRepoWithBranches> fetchUserRepoWithBranches(String username) {
        List<GithubUserRepoResponseDto> userRepos = githubClientProxy.getUserRepos(username)
                .stream()
                .filter(repo -> !repo.fork())
                .toList();

        int numberOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        try {
            List<CompletableFuture<GithubUserRepoWithBranches>> futures = userRepos.stream()
                    .map(userRepo -> CompletableFuture.supplyAsync(() -> {
                        List<GithubUserBranchResponseDto> branchesDto = githubClientProxy.getUserRepoBranches(
                                userRepo.owner().login(), userRepo.name()
                        );
                        List<GithubUserRepoWithBranches.Branch> branches = getUserBranches(branchesDto);

                        return new GithubUserRepoWithBranches(
                                userRepo.name(),
                                userRepo.owner().login(),
                                branches
                        );
                    }, executorService))
                    .toList();

            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } finally {
            executorService.shutdown();
        }
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
