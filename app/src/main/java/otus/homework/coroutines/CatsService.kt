package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Query

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(@Query("max_length") maxLength: Int = 1000) : Fact
}