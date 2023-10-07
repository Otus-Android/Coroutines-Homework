package otus.homework.coroutines.data

import otus.homework.coroutines.data.services.CatsService
import otus.homework.coroutines.presentation.FactsRepository
import otus.homework.coroutines.presentation.Result
import retrofit2.Retrofit

class FactsRepositoryImpl(private val retrofit: Retrofit) :FactsRepository{
    override suspend fun  getFact(): Result<String> {
        val factService = retrofit.create(CatsService::class.java)
      return  try {
             Result.Success(data = factService.getCatFactWithCoroutines(Endpoints.FACT_URL+"fact").fact)
        }catch (e: Throwable){
            Result.Error(throwable = e)
        }
    }
}