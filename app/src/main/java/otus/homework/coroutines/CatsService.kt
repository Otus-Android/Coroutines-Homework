package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET

interface CatsService {
    @GET(".")
    suspend fun getCatFact() : Fact
    @GET("https://aws.random.cat/meow")
    suspend fun getImage(): File
}