package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET

interface CatsFactService {

    @GET("/fact")
    suspend fun getCatFact() : CatsFact

}