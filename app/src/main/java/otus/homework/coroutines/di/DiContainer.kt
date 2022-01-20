package otus.homework.coroutines.di

import otus.homework.coroutines.service.FactsService
import otus.homework.coroutines.service.PicsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitFact by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitPic by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceFact by lazy { retrofitFact.create(FactsService::class.java) }

    val servicePic by lazy { retrofitPic.create(PicsService::class.java) }
}