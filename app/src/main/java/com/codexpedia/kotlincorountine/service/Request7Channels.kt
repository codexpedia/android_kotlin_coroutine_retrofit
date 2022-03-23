package com.codexpedia.kotlincorountine.service

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun loadContributorsChannels(
    service: GitHubService,
    req: RequestData,
    updateResults: (msg: String)->Unit
) {
    coroutineScope {
        val repos = service
            .getOrgRepos(req.org)
            .also { updateResults(constructRepoLog(req, it)) }
            .bodyList()

        val channel = Channel<List<User>>()
        for (repo in repos) {
            launch {
                val users = service.getRepoContributors(req.org, repo.name)
                    .also { updateResults(constructUsersLog(repo, it)) }
                    .bodyList()
                channel.send(users)
            }
        }
        var allUsers = emptyList<User>()
        repeat(repos.size) {
            val users = channel.receive()
            allUsers = (allUsers + users).aggregate()
            updateResults(allUsers.toString())
        }
    }
}
