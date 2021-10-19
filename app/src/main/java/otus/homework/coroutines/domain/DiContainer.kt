package otus.homework.coroutines.domain

import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitFacts by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsFactService by lazy { retrofitFacts.create(CatsFactService::class.java) }

    val imageService: CatsImageService by lazy { retrofitImage.create(CatsImageService::class.java) }
}