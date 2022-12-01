package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsService by lazy { retrofit.create(CatsService::class.java) }

    val imageService: CatsImageService by lazy { retrofit.create(CatsImageService::class.java) }
}