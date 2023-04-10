package otus.homework.coroutines

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.api.PODService
import otus.homework.coroutines.repository.FactRepositoryImpl
import otus.homework.coroutines.repository.PODRepositoryImpl
import otus.homework.coroutines.usecase.CatInfoUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitFact by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private val retrofitPOD by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private val factService by lazy { retrofitFact.create(CatsService::class.java) }
    private val podService by lazy { retrofitPOD.create(PODService::class.java) }
    private val factRepositoryImpl by lazy { FactRepositoryImpl(factService) }
    private val podRepositoryImpl by lazy { PODRepositoryImpl(podService) }
    val catInfoUseCase by lazy {
        CatInfoUseCase(factRepository = factRepositoryImpl, podRepository = podRepositoryImpl)
    }
}
