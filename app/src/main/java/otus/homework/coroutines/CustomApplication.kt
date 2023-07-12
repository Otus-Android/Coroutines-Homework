package otus.homework.coroutines

import android.app.Application
import android.content.Context

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