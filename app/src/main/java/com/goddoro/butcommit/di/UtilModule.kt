package com.goddoro.butcommit.di

import com.goddoro.butcommit.utils.AppPreference
import com.goddoro.butcommit.utils.DateUtil
import com.goddoro.butcommit.utils.ToastUtil
import org.koin.dsl.module


val utilModule = module {

    single { AppPreference(get())}
    single { ToastUtil(get()) }
    single { DateUtil() }
}