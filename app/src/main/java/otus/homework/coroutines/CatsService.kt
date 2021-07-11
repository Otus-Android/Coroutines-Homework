package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("dogs?number=1")
    suspend fun getCatFact(): Response<List<Fact>>

    @GET
    suspend fun getCatImage(@Url url: String = "https://aws.random.cat/meow"): Response<CatPicture>
}