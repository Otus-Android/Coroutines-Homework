package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("dogs?number=1")
    suspend fun getCatFact() : Response<List<Fact>>
}