package com.example.githubtask.feature;

import com.example.githubtask.BaseIntegrationTest;
import com.example.githubtask.infrastructure.controller.dto.response.GithubUserRepoWithBranchesResponseDto;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import com.github.tomakehurst.wiremock.client.WireMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ListGithubRepositoryHappyPathIntegrationTest extends BaseIntegrationTest implements SampleGithubResponse {

    @Test
    public void shouldReturnNonForkRepositoriesWithBranches() throws Exception {
        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/users/abcabcabc/repos"))   // ← tu WireMock.get
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(reposWithOneNonForkAndOneForkJson())));

        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/repos/abcabcabc/sampleRepository/branches"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(branchesWithTwoEntriesJson())));

        ResultActions performGetRepositoriesRequest = mockMvc.perform(
                get("/github/repositories")
                        .param("userName", "abcabcabc")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = performGetRepositoriesRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<GithubUserRepoWithBranchesResponseDto> response =
                objectMapper.readValue(json, new TypeReference<>() {
                });

        assertThat(response).hasSize(1);
        GithubUserRepoWithBranchesResponseDto repo = response.getFirst();

        assertAll(
                () -> assertThat(repo.repositoryName()).isEqualTo("sampleRepository"),
                () -> assertThat(repo.ownerLogin()).isEqualTo("abcabcabc"),

                () -> assertThat(repo.branches()).hasSize(2),

                () -> assertThat(repo.branches())
                        .extracting(GithubUserRepoWithBranchesResponseDto.BranchDto::name)
                        .containsExactlyInAnyOrder("main", "dev"),

                () -> assertThat(repo.branches())
                        .extracting(GithubUserRepoWithBranchesResponseDto.BranchDto::lastCommitSha)
                        .containsExactlyInAnyOrder("1111111", "2222222")
        );
    }


}
