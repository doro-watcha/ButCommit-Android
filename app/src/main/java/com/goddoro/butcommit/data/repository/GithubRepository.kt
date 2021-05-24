package com.goddoro.butcommit.data.repository

import com.goddoro.butcommit.data.model.Commit
import io.reactivex.Completable
import java.util.*

interface GithubRepository {

    suspend fun listCommits(
        username : String,
        startDate : String
    ) : List<Commit>

    suspend fun checkUsername(
        username : String
    ) : Completable
}