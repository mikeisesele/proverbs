package com.michael.proverbs.feature.entrypoint

import android.app.Application
import com.michael.easylog.DefaultLogger
import com.michael.easylog.EasyLog
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        EasyLog.setUp {
            debugMode(true)
            addDefaultLogger((DefaultLogger.DEFAULT_ANDROID))
            filterTag("PROVERBS-LOG")
        }
    }
}
