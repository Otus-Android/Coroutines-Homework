package otus.homework.coroutines.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitFacts by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val retrofitImages by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factsService by lazy { retrofitFacts.create(CatsFactService::class.java) }
    val imagessService by lazy { retrofitImages.create(CatsImageService::class.java) }
}