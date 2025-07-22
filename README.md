# GithubTask Spring Boot App

List repos for a user ep: `/users/{username}/repos` (e.g. `https://api.github.com/users/rlichtarski/repos`) - [docs](https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user)

List branches for the user's repo ep: `/repos/{owner}/{repo}/branches` (e.g.
`https://api.github.com/repos/OWNER/REPO/branches`) - [docs](https://docs.github.com/en/rest/branches/branches?apiVersion=2022-11-28#list-branches)

# Documentation:

This project is seperated into 2 layers - `domain` and `infrastructure`. \
Infrastructure layer is for communicating with the client via rest controller (user) and for communicating with
GithubApi.

Infrastructure is seperated into `client` and `controller`

- `client` is the client side of the app - because this app communicates with the outside (Github) API.
- `controller` is the part of this server - it communicates with client by exposing an endpoint

The infrastructure contains a `GithubRestController` - this is the RestController which exposes @GetMapping
`/github/repositories/{userName}` endpoint.
The `GithubRestController` injects `GithubClientService` service via constructor injection.

This endpoint will return all user's Github repositories (which are not forks), the login of the repo's owner and branch's name and its last
commit sha \
Sample json:

```json
[
  {
    "repositoryName": "string",
    "ownerLogin": "string",
    "branches": [
      {
        "name": "string",
        "lastCommitSha": "string"
      },
      {
        "name": "string",
        "lastCommitSha": "string"
      }
    ]
  },
  {
    "repositoryName": "string",
    "ownerLogin": "string",
    "branches": [
      {
        "name": "string",
        "lastCommitSha": "string"
      }
    ]
  }
]
```

The @GetMapping's method is called `getRepositoriesByUserName`. This method calls the `fetchUserRepoWithBranches` method from
`GithubClientService` class from `domain` layer.

`fetchUserRepoWithBranches` gets the list of user's repositories (via `getUserRepos` method from GithubClientProxy) and
returns repos names and owners. OpenFeign is used for making API calls.

Then, for each repo, there is a call for getting its branches. I created ExecutorConfig for that for threading.

`ExecutorConfig` defines a bean named `githubIoExecutor` – a fixed thread‑pool and shut down automatically with the context. 
Every outbound call to GitHub runs on this pool, so I/O never blocks Spring threads.

There is a `ClientConfig` class, which provides beans for `ObjectMapper` and `ErrorDecoder`. An important Bean is `feignErrorDecoder`
which returns `GithubClientErrorDecoder`.
Because the `Config` class is a configuration for FeignClient, the `GithubClientErrorDecoder` "catches" errors which are
returned
from the requests to Github api. The `GithubClientErrorDecoder` implements ErrorDecoder and thanks to this there is
`decode` method overrided. In case an error comes from any request to Github, the `message` and `http status` is
extracted
and it throws either a GithubApiException or GithubUserNotFoundResponseDto, which is then "caught" inside `GithubExceptionHandler` RestControllerAdvice class,
which then returns a ResponseEntity of the given exception with the given http status and message.

And after all of this magic, the `GithubRestController` maps domain classes to List of dtos and return a ResponseEntity of
200 with the response data.