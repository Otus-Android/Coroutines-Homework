package otus.homework.coroutines

import android.app.Application

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object{
        var INSTANCE: App? = null
    }
}