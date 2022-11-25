package com.example.domain.di

import com.example.domain.usecases.ProductDetailsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::ProductDetailsUseCase)

}