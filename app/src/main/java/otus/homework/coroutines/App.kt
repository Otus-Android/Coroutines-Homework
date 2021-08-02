package otus.homework.coroutines

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import otus.homework.coroutines.di.dataSourceModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules( listOf(
                    dataSourceModule
                )
            )
        }
    }
}
