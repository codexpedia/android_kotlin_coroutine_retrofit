package com.codexpedia.kotlincorountine.service

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun loadContributorsCallbacks(service: GitHubService, req: RequestData, updateResults: (msg: String)->Unit) {
    service.getOrgReposCall(req.org).onResponse { responseRepos ->
        updateResults(constructRepoLog(req, responseRepos))
        val repos = responseRepos.bodyList()
        val allUsers = mutableListOf<User>()

        repos.forEachIndexed { index, repo ->
            service.getRepoContributorsCall(req.org, repo.name).onResponse { responseUsers ->
                updateResults(constructUsersLog(repo, responseUsers))
                val users = responseUsers.bodyList()
                allUsers += users
                if (index == repos.size - 1) {
                    updateResults(allUsers.aggregate().toString())
                }
            }
        }
    }
}

inline fun <T> Call<T>.onResponse(crossinline callback: (Response<T>) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("Call failed", t.toString())
        }
    })
}
