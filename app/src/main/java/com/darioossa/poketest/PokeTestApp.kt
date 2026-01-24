package com.darioossa.poketest

import android.app.Application
import com.darioossa.poketest.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokeTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PokeTestApp)
            modules(appModules)
        }
    }
}
