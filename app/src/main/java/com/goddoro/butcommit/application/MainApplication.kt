package com.goddoro.butcommit.application

import android.app.Application
import com.goddoro.butcommit.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


/**
 * created By DORO 5/21/21
 */



class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        inject()
    }

    private fun inject() {

        startKoin {
            androidContext(this@MainApplication)
            androidLogger(Level.INFO)
            modules(
                listOf(
                    viewModelModule,
                    utilModule,
                    apiModule,
                    repositoryModule,
                    networkModule
                )
            )
        }
    }
}