package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("meme")
    suspend fun getCatFact() : Response<Meme>
}