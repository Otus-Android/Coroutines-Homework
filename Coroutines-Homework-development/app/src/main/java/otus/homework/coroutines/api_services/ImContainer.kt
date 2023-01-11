package otus.homework.coroutines.api_services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ImContainer {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(ImageCatsService::class.java) }
}