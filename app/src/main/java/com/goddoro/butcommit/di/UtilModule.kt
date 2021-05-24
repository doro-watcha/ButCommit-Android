package com.goddoro.butcommit.di

import com.goddoro.butcommit.utils.AppPreference
import org.koin.dsl.module


val utilModule = module {

    single { AppPreference(get())}
}