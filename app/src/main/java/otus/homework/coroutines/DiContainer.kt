package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val FACT_URL = "https://catfact.ninja/"
private const val IMAGE_URL = "https://api.thecatapi.com/"

class DiContainer {

    private fun getRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsFactService by lazy { getRetrofit(FACT_URL).create(CatsFactService::class.java) }
    val imageService: CatsImageService by lazy { getRetrofit(IMAGE_URL).create(CatsImageService::class.java) }
}
