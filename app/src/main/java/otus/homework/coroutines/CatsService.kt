package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact?

    @GET("meow")
    suspend fun getPic(): Picture?
}