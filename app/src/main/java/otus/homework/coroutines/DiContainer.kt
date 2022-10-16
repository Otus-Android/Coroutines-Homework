package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {
    private val gson = GsonConverterFactory.create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(gson)
            .build()
    }

    private val imgRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(gson)
            .build()
    }

    val factService: CatsService by lazy { retrofit.create(CatsService::class.java) }

    val imgService: CatsImageService by lazy { imgRetrofit.create(CatsImageService::class.java) }
}
