package com.goddoro.butcommit.di

import com.goddoro.butcommit.data.repository.GithubRepository
import com.goddoro.butcommit.data.repository.UserRepository
import com.goddoro.butcommit.data.repositoryImpl.GithubRepositoryImpl
import com.goddoro.butcommit.data.repositoryImpl.UserRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {


    single { GithubRepositoryImpl(get()) } bind GithubRepository::class
    single { UserRepositoryImpl(get()) } bind UserRepository::class

}