package otus.homework.coroutines.data

import otus.homework.coroutines.domain.CatImage
import otus.homework.coroutines.domain.Fact
import retrofit2.http.GET

interface CatsService {
    @GET("fact")
    suspend fun getCatFact(): Fact

    @GET("https://api.thecatapi.com/v1/images/search")
    suspend fun getCatImage(): List<CatImage>
}