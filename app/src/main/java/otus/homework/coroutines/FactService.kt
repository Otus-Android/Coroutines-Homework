package otus.homework.coroutines

import retrofit2.http.GET

interface FactService {
    @GET("fact")
    suspend fun getCatFact():Fact
}