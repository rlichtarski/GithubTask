package com.example.githubtask.feature;

import com.example.githubtask.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;

public class ListGithubRepositoryHappyPathIntegrationTest extends BaseIntegrationTest {

    @Test
    public void listGithubRepositoryHappyPathIntegrationTest() {
        assertNotNull(mockMvc);
    }


}
