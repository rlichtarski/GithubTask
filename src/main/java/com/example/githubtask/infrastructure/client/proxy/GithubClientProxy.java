package com.example.githubtask.infrastructure.client.proxy;

import com.example.githubtask.infrastructure.client.config.ClientConfig;
import com.example.githubtask.infrastructure.client.proxy.dto.GithubUserBranchResponseDto;
import com.example.githubtask.infrastructure.client.proxy.dto.GithubUserRepoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GithubClient", url = "${githubclient.proxy.url}", configuration = ClientConfig.class)
public interface GithubClientProxy {

    @GetMapping("/users/{username}/repos")
    List<GithubUserRepoResponseDto> getUserRepos(
            @PathVariable("username") String username
    );

    @GetMapping("/repos/{owner}/{repo}/branches")
    List<GithubUserBranchResponseDto> getUserRepoBranches(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );

}
