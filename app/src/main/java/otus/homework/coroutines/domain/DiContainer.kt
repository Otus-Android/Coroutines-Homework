package otus.homework.coroutines.domain

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val catsService: CatsService by lazy { retrofit.create(CatsService::class.java) }
    val catsIconService: CatsIconService by lazy { iconRetrofit.create(CatsIconService::class.java) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // https://aws.random.cat/meow - не работал этот сайт, использовал другой
    private val iconRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}