package otus.homework.coroutines

import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsFactRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val catsImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsFactService: CatsFactService by lazy { catsFactRetrofit.create(CatsFactService::class.java) }

    val catsImageService: CatsImageService by lazy { catsImageRetrofit.create(CatsImageService::class.java) }
}
