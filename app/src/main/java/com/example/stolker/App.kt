package com.example.stolker

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.stolker.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, domainModule, presentationModule)
        }
    }

}