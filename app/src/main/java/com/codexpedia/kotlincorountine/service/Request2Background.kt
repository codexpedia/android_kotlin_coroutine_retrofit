package com.codexpedia.kotlincorountine.service

import kotlin.concurrent.thread

fun loadContributorsBackground(service: GitHubService, req: RequestData, updateResults: (msg: String)->Unit) {
    thread {
        loadContributorsBlocking(service, req, updateResults)
    }
}