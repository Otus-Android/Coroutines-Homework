package otus.homework.coroutines.api

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}