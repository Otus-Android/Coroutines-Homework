package otus.homework.coroutines.di

import otus.homework.coroutines.model.mapper.FactDtoToEntityMapper
import otus.homework.coroutines.model.network.CatsApiService
import otus.homework.coroutines.model.network.CatsFactService
import otus.homework.coroutines.model.repository.CatsRepository
import otus.homework.coroutines.model.repository.CatsRepositoryImpl
import otus.homework.coroutines.model.usecase.CatsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val catsUseCase by lazy {
        CatsUseCase(catsRepository, FactDtoToEntityMapper())
    }

    private val catsRepository: CatsRepository by lazy {
        CatsRepositoryImpl(catsFactService, catsApiService)
    }

    private val catsFactService by lazy {
        catFactRetrofitClient.create(CatsFactService::class.java)
    }
    private val catsApiService by lazy {
        catApiRetrofitClient.create(CatsApiService::class.java)
    }

    private val catFactRetrofitClient by lazy {
        baseRetrofitBuilder
            .baseUrl("https://catfact.ninja/")
            .build()
    }

    private val catApiRetrofitClient by lazy {
        baseRetrofitBuilder
            .baseUrl("https://api.thecatapi.com/v1/")
            .build()
    }

    private val baseRetrofitBuilder by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
    }
}
