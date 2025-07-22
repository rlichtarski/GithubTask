package com.example.githubtask.feature;

public interface SampleGithubResponse {

    default String reposWithOneNonForkAndOneForkJson() {
        return """
               [
                 { "name": "sampleRepository",
                   "fork": false,
                   "owner": { "login": "abcabcabc" } },

                 { "name": "forkedRepository",
                   "fork": true,
                   "owner": { "login": "abcabcabc" } }
               ]
               """.trim();
    }

    default String branchesWithTwoEntriesJson() {
        return """
               [
                 { "name": "main",    "commit": { "sha": "1111111" } },
                 { "name": "dev",     "commit": { "sha": "2222222" } }
               ]
               """.trim();
    }
}

