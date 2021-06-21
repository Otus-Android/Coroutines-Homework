package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Response<Fact>

    @GET
    suspend fun getRandomCatImage(@Url url: String = "https://aws.random.cat/meow"): Response<RandomCatImage>
}