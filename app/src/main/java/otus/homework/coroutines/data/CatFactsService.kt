package otus.homework.coroutines.data

import otus.homework.coroutines.models.Fact
import otus.homework.coroutines.models.Result
import retrofit2.http.GET

interface CatFactsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}