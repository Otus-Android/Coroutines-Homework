package otus.homework.coroutines

import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.FileMeow
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

    @GET
    suspend fun getPicture(@Url url: String) : FileMeow
}