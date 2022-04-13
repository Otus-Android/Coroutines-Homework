package otus.homework.coroutines.di

import otus.homework.coroutines.CatsService
import otus.homework.coroutines.presentation.PresenterScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val presenterScope by lazy(LazyThreadSafetyMode.NONE) {
        PresenterScope()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }
}