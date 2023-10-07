package otus.homework.coroutines.di

import otus.homework.coroutines.data.Endpoints
import otus.homework.coroutines.data.FactsRepositoryImpl
import otus.homework.coroutines.data.PictureRepositoryImpl
import otus.homework.coroutines.presentation.FactsRepository
import otus.homework.coroutines.presentation.PictureRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Endpoints.FACT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pictureRepository : PictureRepository by lazy {
        PictureRepositoryImpl(retrofit)
    }

    val factsRepository : FactsRepository by lazy {
        FactsRepositoryImpl(retrofit)
    }
}