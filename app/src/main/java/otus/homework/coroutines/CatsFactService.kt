package otus.homework.coroutines

import otus.homework.coroutines.model.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact() : Response<Fact>
}