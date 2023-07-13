package otus.homework.coroutines.utils

import android.app.Application
import android.content.Context
import otus.homework.coroutines.di.DiContainer

class CustomApplication : Application() {

    lateinit var diContainer: DiContainer

    override fun onCreate() {
        super.onCreate()
        diContainer = DiContainer(this)
    }

    companion object {

        fun diContainer(context: Context) =
            (context.applicationContext as CustomApplication).diContainer
    }
}