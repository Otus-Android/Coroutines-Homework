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

    private val retrofitCatsImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/") // Базовый URL для картинок
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    val imageService by lazy { retrofitCatsImage.create(CatsImageService::class.java) }
}

