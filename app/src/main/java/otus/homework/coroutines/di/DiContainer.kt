package otus.homework.coroutines.di

import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.data.CatImagesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val catFactsService by lazy {
        Retrofit.Builder()
            .baseUrl(FACT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatFactsService::class.java)
    }

    val catImagesService by lazy {
        Retrofit.Builder()
            .baseUrl(IMAGE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatImagesService::class.java)
    }

    companion object {
        private const val FACT_URL = "https://catfact.ninja/"
        private const val IMAGE_URL = "https://api.thecatapi.com/v1/images/"
    }
}
