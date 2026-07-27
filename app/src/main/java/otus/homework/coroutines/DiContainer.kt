package otus.homework.coroutines

import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.PicturesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val picturesRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsService by lazy { catsRetrofit.create(CatsService::class.java) }
    val picturesService by lazy { picturesRetrofit.create(PicturesService::class.java) }
}