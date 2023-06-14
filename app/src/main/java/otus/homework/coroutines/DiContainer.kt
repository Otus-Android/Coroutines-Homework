package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsFactRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsFactService by lazy {
        catsFactRetrofit.create(CatsFactService::class.java)
    }

    private val catsImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val imageService: CatsImageLinkService by lazy {
        catsImageRetrofit.create(CatsImageLinkService::class.java)
    }
}