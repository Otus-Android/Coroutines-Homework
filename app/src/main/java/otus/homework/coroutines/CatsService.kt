package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET
    suspend fun getCatImage(@Url url: String = "https://aws.random.cat/meow") : Image
}