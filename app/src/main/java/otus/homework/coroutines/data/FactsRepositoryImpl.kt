package otus.homework.coroutines.data

import otus.homework.coroutines.data.model.Fact
import otus.homework.coroutines.data.services.CatsService
import otus.homework.coroutines.presentation.FactsRepository
import retrofit2.Retrofit

class FactsRepositoryImpl(private val retrofit: Retrofit) :FactsRepository{
    override suspend fun getFact(): String {
        val factService = retrofit.create(CatsService::class.java)
        return factService.getCatFactWithCoroutines(Endpoints.FACT_URL+"fact").fact
    }

}