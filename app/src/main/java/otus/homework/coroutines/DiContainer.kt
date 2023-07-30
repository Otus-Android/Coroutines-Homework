package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun retrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceFact: CatsFactService by lazy { retrofit(FACT_URL).create(CatsFactService::class.java) }
    val serviceImage: CatsImageService by lazy { retrofit(IMAGE_URL).create(CatsImageService::class.java) }

    companion object {
        private const val FACT_URL = "https://catfact.ninja/"
        private const val IMAGE_URL = "https://api.thecatapi.com/"
    }
}