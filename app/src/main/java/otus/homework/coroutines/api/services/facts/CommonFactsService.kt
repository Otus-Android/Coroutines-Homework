package otus.homework.coroutines.api.services.facts

import otus.homework.coroutines.dtos.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CommonFactsService : IFactsService {

    @GET("fact")
    override suspend fun getCatFact(): Response<Fact>
}