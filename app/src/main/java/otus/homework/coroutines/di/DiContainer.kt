package otus.homework.coroutines.di

import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.data.network.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val imageServiceRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }
    val catsImageService: CatsImageService by lazy {
        imageServiceRetrofit.create(CatsImageService::class.java)
    }
}