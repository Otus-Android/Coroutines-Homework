package otus.homework.coroutines.api

import otus.homework.coroutines.models.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Response<Fact>

    
}