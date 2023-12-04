package otus.homework.coroutines.api

import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}