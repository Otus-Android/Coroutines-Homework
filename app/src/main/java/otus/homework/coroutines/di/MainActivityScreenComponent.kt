package otus.homework.coroutines.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import otus.homework.coroutines.CatsPresenter
import otus.homework.coroutines.CrashAnalyticManager
import otus.homework.coroutines.network.services.CatsService
import otus.homework.coroutines.network.services.RandomCatImageService


class MainActivityScreenModule(
    private val mainActivityScreenDependencies: MainActivityScreenDependencies
) {
    private fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    private fun provideCoroutineName(): CoroutineName = CoroutineName("CatsCoroutine")

    private fun provideCoroutineScope(
        dispatcher: CoroutineDispatcher = provideCoroutineDispatcher(),
        coroutineName: CoroutineName = provideCoroutineName(),
    ): CoroutineScope = CoroutineScope(dispatcher + coroutineName)

    fun providePresenter(
        crashMonitor: CrashAnalyticManager = mainActivityScreenDependencies.crashAnalyticManager,
        catsService: CatsService = mainActivityScreenDependencies.catsService,
        randomCatImageService: RandomCatImageService =
            mainActivityScreenDependencies.randomCatImageService,
        coroutineScope: CoroutineScope = provideCoroutineScope()
    ): CatsPresenter = CatsPresenter(
        crashMonitor = crashMonitor,
        catsService = catsService,
        randomCatImageService = randomCatImageService,
        scope = coroutineScope
    )
}

interface MainActivityScreenDependencies {
    val catsService: CatsService
    val randomCatImageService: RandomCatImageService
    val crashAnalyticManager: CrashAnalyticManager
}

interface MainActivityScreenComponent {
    fun providePresenter(): CatsPresenter
}

class MainActivityScreenComponentImpl(
    private val mainActivityScreenModule: MainActivityScreenModule
) : MainActivityScreenComponent {
    override fun providePresenter(): CatsPresenter {
        return mainActivityScreenModule.providePresenter()
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