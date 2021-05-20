package com.example.hammered

import android.app.Application
import timber.log.Timber

class HammeredApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}