package com.codexpedia.kotlincorountine.service


suspend fun loadContributorsSuspend(service: GitHubService, req: RequestData, updateResults: (msg: String)->Unit): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { updateResults(constructRepoLog(req, it)) }
        .body() ?: listOf()

    return repos.flatMap { repo ->
        service
            .getRepoContributors(req.org, repo.name)
            .also { updateResults(constructUsersLog(repo, it)) }
            .bodyList()
    }.aggregate()
}