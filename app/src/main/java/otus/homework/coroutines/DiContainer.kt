package otus.homework.coroutines

import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun getRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsFactService by lazy { getRetrofit(URL_FACT).create(CatsFactService::class.java)}
    val catsImageService by lazy { getRetrofit(URL_IMAGE).create(CatsImageService::class.java)}

    companion object {
        private const val URL_FACT = "https://catfact.ninja/"
        private const val URL_IMAGE = "https://api.thecatapi.com/"
    }
}