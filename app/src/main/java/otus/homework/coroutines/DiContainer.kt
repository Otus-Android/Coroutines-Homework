package otus.homework.coroutines

import otus.homework.coroutines.data.CatsRepositoryImpl
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.utils.CoroutineDispatchersImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }
    val coroutineDispatchers by lazy { CoroutineDispatchersImpl() }
    val repository by lazy { CatsRepositoryImpl(service, coroutineDispatchers) }
}