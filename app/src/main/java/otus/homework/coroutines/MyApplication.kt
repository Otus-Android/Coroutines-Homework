package otus.homework.coroutines

import android.app.Application

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CrashMonitor.context = applicationContext
    }
}