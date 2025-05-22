package otus.homework.coroutines

import otus.homework.coroutines.dto.CatImage
import otus.homework.coroutines.dto.Fact
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact

    @GET
    suspend fun getCatImage(@Url url: String = "https://api.thecatapi.com/v1/images/search"): List<CatImage>
}