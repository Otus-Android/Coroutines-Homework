package otus.homework.coroutines.api.services.facts

import otus.homework.coroutines.api.IRetrofitService
import otus.homework.coroutines.Fact
import retrofit2.Response

interface IFactsService:IRetrofitService {
    suspend fun getCatFact() : Response<Fact>
}
