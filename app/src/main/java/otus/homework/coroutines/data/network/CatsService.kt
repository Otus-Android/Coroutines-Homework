package otus.homework.coroutines.data.network

import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}