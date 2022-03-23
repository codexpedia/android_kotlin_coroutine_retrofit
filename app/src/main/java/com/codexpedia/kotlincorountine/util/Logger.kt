package com.codexpedia.kotlincorountine.service

import retrofit2.Response

fun constructRepoLog(req: RequestData, response: Response<List<Repo>>): String {
    val repos = response.body()
    return if (!response.isSuccessful || repos == null) {
        "Failed loading repos for ${req.org} with response: '${response.code()}: ${response.message()}'";
    }
    else {
        "${req.org}: loaded ${repos.size} repos"
    }
}

fun constructUsersLog(repo: Repo, response: Response<List<User>>): String {
    val users = response.body()
    return if (!response.isSuccessful || users == null) {
        "Failed loading contributors for ${repo.name} with response '${response.code()}: ${response.message()}'"
    }
    else {
        "${repo.name}: loaded ${users.size} contributors"
    }
}