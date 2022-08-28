package otus.homework.coroutines.di

import otus.homework.coroutines.network.CatImageService
import otus.homework.coroutines.network.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun retrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy {
        retrofit("https://cat-fact.herokuapp.com/facts/")
            .create(CatsService::class.java)
    }

    val imageCatsService by lazy {
        retrofit("https://aws.random.cat/")
            .create(CatImageService::class.java)
    }
}