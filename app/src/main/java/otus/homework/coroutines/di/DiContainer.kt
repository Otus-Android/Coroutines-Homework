package otus.homework.coroutines.di

import otus.homework.coroutines.CrashAnalyticManager
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.network.NetworkMap
import otus.homework.coroutines.network.utils.RetrofitBuilder
import otus.homework.coroutines.network.services.CatsService
import otus.homework.coroutines.network.services.RandomCatImageService

class DiContainer : MainActivityScreenDependencies {

    override val catsService: CatsService by lazy {
        val retrofit = RetrofitBuilder.buildWithSpecificHost(NetworkMap.FACT_BASE_URL)
        retrofit.create(CatsService::class.java)
    }

    override val randomCatImageService: RandomCatImageService by lazy {
        val retrofit = RetrofitBuilder.buildWithSpecificHost(NetworkMap.RANDOM_CAT_IMAGE_BASE_URL)
        retrofit.create(RandomCatImageService::class.java)
    }

    override val crashAnalyticManager: CrashAnalyticManager = CrashMonitor
}