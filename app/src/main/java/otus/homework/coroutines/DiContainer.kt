package otus.homework.coroutines

import otus.homework.coroutines.network.CatsService
import otus.homework.coroutines.network.ImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val catsService: CatsService by lazy {
        catsRetrofit.create(CatsService::class.java)
    }

    val imageService: ImageService by lazy {
        imageRetrofit.create(ImageService::class.java)
    }

    private val catsRetrofit by lazy {
        baseRetrofitBuilder
            .baseUrl("https://catfact.ninja/")
            .build()
    }

    private val imageRetrofit by lazy {
        baseRetrofitBuilder
            .baseUrl("https://aws.random.cat/")
            .build()
    }

    private val baseRetrofitBuilder by lazy {
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
    }

}