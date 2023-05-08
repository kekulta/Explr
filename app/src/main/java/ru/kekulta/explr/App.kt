package ru.kekulta.explr

import android.app.Application
import android.util.Log
import ru.kekulta.explr.di.MainServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")
        MainServiceLocator.initDi(this)
    }

    companion object {
        const val LOG_TAG = "Application"
    }
}