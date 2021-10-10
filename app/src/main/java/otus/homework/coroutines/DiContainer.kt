package otus.homework.coroutines

import otus.homework.coroutines.data.CatsRepositoryImpl
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.utils.CoroutineDispatchersImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.CAT_FACT_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }
    val repository by lazy { CatsRepositoryImpl(service) }
    val coroutineDispatchers by lazy { CoroutineDispatchersImpl() }
}