package com.deema.sdkandroid

import android.app.Application
import timber.log.Timber

class DeemaSdkDemoApp : Application(){

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}