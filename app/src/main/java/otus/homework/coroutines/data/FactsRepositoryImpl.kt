package otus.homework.coroutines.data

import otus.homework.coroutines.data.services.CatsService
import otus.homework.coroutines.presentation.FactsRepository
import otus.homework.coroutines.presentation.Result
import otus.homework.coroutines.presentation.model.FactModel

class FactsRepositoryImpl(private val service: CatsService) :FactsRepository{
    override suspend fun getFact(): Result<FactModel> {
        return try {
            val fact = service.getCatFactWithCoroutines(FACT_URL + "fact")
            val factModel = FactModel(fact.fact)
            Result.Success(data = factModel)
        } catch (e: Throwable) {
            Result.Error(throwable = e)
        }
    }
    companion object{
        private const val FACT_URL = "https://catfact.ninja/"
    }
}