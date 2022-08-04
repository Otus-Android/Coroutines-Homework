package otus.homework.coroutines

import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.api.ImagesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    companion object {
        private const val FACT_SERVICE_URL = "https://cat-fact.herokuapp.com/facts/"
        private const val IMAGE_SERVICE_URL = "https://aws.random.cat/"
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsService by lazy { createRetrofit(FACT_SERVICE_URL).create(CatsService::class.java) }
    val imageService: ImagesService by lazy { createRetrofit(IMAGE_SERVICE_URL).create(ImagesService::class.java) }
}
