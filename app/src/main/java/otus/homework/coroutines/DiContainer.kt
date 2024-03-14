package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitCat by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofitCat.create(CatsService::class.java) }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val image: CatsImage by lazy { retrofitImage.create(CatsImage::class.java) }
}