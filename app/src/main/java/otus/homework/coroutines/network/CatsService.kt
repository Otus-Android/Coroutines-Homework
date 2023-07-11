package otus.homework.coroutines.network

import otus.homework.coroutines.network.models.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}
