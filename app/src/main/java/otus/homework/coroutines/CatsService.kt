package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(@Query("max_length") maxLength: Int = 1000) : Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getCatImage() : Image
}