package otus.homework.coroutines

import retrofit2.http.GET

interface CatsFactsService {

    @GET("fact")
    suspend fun getCatFact() : FactModel

}