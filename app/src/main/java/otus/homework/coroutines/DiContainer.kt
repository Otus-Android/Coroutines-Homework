package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

class DiContainer {

    private val catsFactRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsService by lazy { catsFactRetrofit.create(CatsService::class.java) }

    private val catsImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsImageService by lazy { catsImageRetrofit.create(CatsImageService::class.java) }

    
}
