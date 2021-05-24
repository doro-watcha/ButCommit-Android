package com.goddoro.butcommit.data.repositoryImpl

import com.goddoro.butcommit.data.api.GithubAPI
import com.goddoro.butcommit.data.model.Commit
import com.goddoro.butcommit.data.repository.GithubRepository
import com.goddoro.butcommit.utils.filterValueNotNull
import io.reactivex.Completable

class GithubRepositoryImpl ( val api : GithubAPI) : GithubRepository{

    override suspend fun listCommits(username: String, startDate : String ): List<Commit> {

        val params = hashMapOf(
            "username" to username,
            "startDate" to startDate
        ).filterValueNotNull()

        return api.listCommits(params).unWrapData().commits
    }

    override suspend fun checkUsername(username: String): Completable {
        return api.checkUsername(username).unWrapCompletable()
    }
}