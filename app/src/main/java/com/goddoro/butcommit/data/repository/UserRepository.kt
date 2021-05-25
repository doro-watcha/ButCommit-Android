package com.goddoro.butcommit.data.repository

import io.reactivex.Completable

interface UserRepository {

    suspend fun register (
        username : String,
        fcmToken : String
    ) : Completable

    suspend fun update (
        _username : String,
        username : String? = null,
        fcmToken : String? = null,
        isDoing : Boolean? = null
    ) : Completable
}