package com.codexpedia.kotlincorountine.service

import retrofit2.Response

fun loadContributorsBlocking(service: GitHubService, req: RequestData, updateResults: (msg: String)->Unit) : List<User> {
    val repos = service
        .getOrgReposCall(req.org)
        .execute() // Executes request and blocks the current thread
        .also { updateResults(constructRepoLog(req, it)) }
        .body() ?: listOf()

    return repos.flatMap { repo ->
        service
            .getRepoContributorsCall(req.org, repo.name)
            .execute() // Executes request and blocks the current thread
            .also { updateResults(constructUsersLog(repo, it))}
            .bodyList()
    }.aggregate()
}

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: listOf()
}