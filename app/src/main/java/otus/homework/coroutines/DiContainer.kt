package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsServiceRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://dog-facts-api.herokuapp.com/api/v1/resources/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsService by lazy { catsServiceRetrofit.create(CatsService::class.java) }

    private val imagesServiceRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val imagesService by lazy { imagesServiceRetrofit.create(ImagesService::class.java) }
}