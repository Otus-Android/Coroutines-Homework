package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Url


interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact


    @GET
    suspend fun getPicture(@Url url: String) : FileMeow


}