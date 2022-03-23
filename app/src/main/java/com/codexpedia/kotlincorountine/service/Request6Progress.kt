package com.codexpedia.kotlincorountine.service

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: (msg: String)->Unit
) {
    val repos = service
        .getOrgRepos(req.org)
        .also { updateResults(constructRepoLog(req, it)) }
        .bodyList()

    var allUsers = emptyList<User>()
    for ((index, repo) in repos.withIndex()) {
        val users = service.getRepoContributors(req.org, repo.name)
            .also { updateResults(constructUsersLog(repo, it)) }
            .bodyList()

        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers.toString())
    }
}
