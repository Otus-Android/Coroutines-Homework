package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
   suspend fun getCatFact() : Fact

    @GET("https://api.thecatapi.com/v1/images/search")
    suspend fun getCatFactImg() : List<Img>
}