package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

    @GET("v1/images/search")
    suspend fun getCatImage(): List<CatImage>

}