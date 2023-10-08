package otus.homework.coroutines.data

import otus.homework.coroutines.Fact
import retrofit2.http.GET

interface CatFactsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}