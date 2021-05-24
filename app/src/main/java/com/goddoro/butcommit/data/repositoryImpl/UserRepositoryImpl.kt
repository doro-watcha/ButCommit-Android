package com.goddoro.butcommit.data.repositoryImpl

import com.goddoro.butcommit.data.api.UserAPI
import com.goddoro.butcommit.data.repository.UserRepository
import com.goddoro.butcommit.utils.filterValueNotNull
import io.reactivex.Completable

class UserRepositoryImpl ( val api : UserAPI ) : UserRepository {

    override suspend fun register(username: String, fcmToken: String): Completable {
        val params = hashMapOf(
            "username" to username,
            "fcmToken" to fcmToken
        ).filterValueNotNull()

        return api.register(params).unWrapCompletable()
    }

    override suspend fun update(username: String?, fcmToken: String?, isDoing: Boolean?): Completable {

        val params = hashMapOf(
            "username" to username,
            "fcmToken" to fcmToken,
            "isDoing" to isDoing
        ).filterValueNotNull()

        return api.update(params).unWrapCompletable()
    }
}