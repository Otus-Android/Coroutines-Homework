package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val factRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val factService: CatsFactService by lazy { factRetrofit.create(CatsFactService::class.java) }

    private val imageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val imageService: CatsImageService by lazy { imageRetrofit.create(CatsImageService::class.java) }

    val catsRepository by lazy {
        CatsRepository(
            factService = factService,
            imageService = imageService
        )
    }
}