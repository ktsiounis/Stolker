package com.example.stolker.di

import com.example.stolker.ui.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    viewModelOf(::MainViewModel)

}