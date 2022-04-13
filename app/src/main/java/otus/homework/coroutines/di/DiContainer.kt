package otus.homework.coroutines.di

import otus.homework.coroutines.CatsService
import otus.homework.coroutines.CrashAnalyticManager
import otus.homework.coroutines.CrashMonitor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    val crashAnalyticManager: CrashAnalyticManager = CrashMonitor
}