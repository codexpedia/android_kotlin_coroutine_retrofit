package com.codexpedia.kotlincorountine.service

import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData, updateResults: (msg: String)->Unit): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org)
        .also { updateResults(constructRepoLog(req, it)) }
        .bodyList()

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        async {
            updateResults("starting loading for ${repo.name}")
            delay(3000)
            service.getRepoContributors(req.org, repo.name)
                .also { updateResults(constructUsersLog(repo, it)) }
                .bodyList()
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}