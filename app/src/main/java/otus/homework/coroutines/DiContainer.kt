package otus.homework.coroutines

import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsPhotoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun createRetrofit(url: String) =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val factService by lazy { createRetrofit(FACT_URL).create(CatsFactService::class.java) }
    val photoService by lazy { createRetrofit(PHOTO_URL).create(CatsPhotoService::class.java) }

    companion object {
        private const val PHOTO_URL = "https://aws.random.cat/"
        private const val FACT_URL = "https://cat-fact.herokuapp.com/facts/"
    }
}