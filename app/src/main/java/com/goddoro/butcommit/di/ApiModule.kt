package com.goddoro.butcommit.di

import com.goddoro.butcommit.data.api.GithubAPI
import com.goddoro.butcommit.data.api.UserAPI
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module{

    single { get<Retrofit>().create(GithubAPI::class.java) }
    single { get<Retrofit>().create(UserAPI::class.java)}

}