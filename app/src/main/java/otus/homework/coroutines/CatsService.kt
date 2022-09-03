package otus.homework.coroutines

import otus.homework.coroutines.models.CatsFact
import otus.homework.coroutines.models.CatsImage
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("/fact?max_length=140")
    suspend fun getCatFact() : CatsFact

    @GET
    suspend fun getCatImage(@Url url: String) : CatsImage
}