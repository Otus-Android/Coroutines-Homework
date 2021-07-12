package otus.homework.coroutines

import otus.homework.coroutines.network.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("dogs?number=1")
    suspend fun getCatFact() : Response<List<Fact>>
}