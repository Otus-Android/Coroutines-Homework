package otus.homework.coroutines.util

import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.data.PicsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsService by lazy { getRetrofit(BASE_URL_CATS).create(CatsService::class.java) }

    val picsService by lazy { getRetrofit(BASE_URL_PICS).create(PicsService::class.java) }

    companion object {
        private const val BASE_URL_CATS = "https://catfact.ninja/"
        private const val BASE_URL_PICS = "https://api.thecatapi.com/v1/"
    }

}