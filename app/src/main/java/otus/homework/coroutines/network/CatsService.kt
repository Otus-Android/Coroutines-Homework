package otus.homework.coroutines.network

import otus.homework.coroutines.model.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Response<Fact>
}