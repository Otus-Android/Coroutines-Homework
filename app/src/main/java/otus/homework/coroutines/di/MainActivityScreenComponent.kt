package otus.homework.coroutines.di

import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.CatsViewModelFactory
import otus.homework.coroutines.CrashAnalyticManager
import otus.homework.coroutines.network.services.CatsService
import otus.homework.coroutines.network.services.RandomCatImageService


class MainActivityScreenModule(
    private val mainActivityScreenDependencies: MainActivityScreenDependencies
) {
    fun provideViewModelFactory(
        crashMonitor: CrashAnalyticManager = mainActivityScreenDependencies.crashAnalyticManager,
        catsService: CatsService = mainActivityScreenDependencies.catsService,
        randomCatImageService: RandomCatImageService =
            mainActivityScreenDependencies.randomCatImageService,
    ): ViewModelProvider.Factory {
        return CatsViewModelFactory(
            crashAnalyticManager = crashMonitor,
            catsService = catsService,
            randomCatImageService = randomCatImageService
        )
    }
}

interface MainActivityScreenDependencies {
    val catsService: CatsService
    val randomCatImageService: RandomCatImageService
    val crashAnalyticManager: CrashAnalyticManager
}

interface MainActivityScreenComponent {
    fun provideViewModelFactory(): ViewModelProvider.Factory
}

class MainActivityScreenComponentImpl(
    private val mainActivityScreenModule: MainActivityScreenModule
) : MainActivityScreenComponent {

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return mainActivityScreenModule.provideViewModelFactory()
    }

    companion object Factory {
        fun create(
            mainActivityScreenDependencies: MainActivityScreenDependencies
        ): MainActivityScreenComponent {
            val mainActivityScreenModule = MainActivityScreenModule(mainActivityScreenDependencies)
            return MainActivityScreenComponentImpl(
                mainActivityScreenModule = mainActivityScreenModule
            )
        }
    }
}