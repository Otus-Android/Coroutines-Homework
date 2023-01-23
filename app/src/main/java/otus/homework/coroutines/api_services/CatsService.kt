package otus.homework.coroutines.api_services

import otus.homework.coroutines.data.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}