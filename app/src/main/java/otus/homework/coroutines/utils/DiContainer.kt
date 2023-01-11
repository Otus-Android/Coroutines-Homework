package otus.homework.coroutines.utils

import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val factRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val imageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val catsService by lazy { factRetrofit.create(CatsService::class.java) }
    val imageService by lazy { imageRetrofit.create(ImageService::class.java) }
}