package com.goddoro.butcommit.data.repository

import io.reactivex.Completable

interface UserRepository {

    suspend fun register (
        username : String,
        fcmToken : String
    ) : Completable

    suspend fun update (
        username : String? = null,
        fcmToken : String? = null,
        isDoing : Boolean? = null
    ) : Completable
}