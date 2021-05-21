package com.goddoro.butcommit.di

import androidx.lifecycle.ViewModel
import com.goddoro.butcommit.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * created By DORO 5/21/21
 */


val viewModelModule = module{


    viewModel { MainViewModel() }
}