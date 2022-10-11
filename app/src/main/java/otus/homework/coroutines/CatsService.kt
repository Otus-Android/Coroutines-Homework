package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Response<Fact>

    @GET("https://aws.random.cat/meow")
    suspend fun getRandomImage(): ImageDescription

    @GET("https://catfact.ninja/fact")
    suspend fun getCatFactReserve(): FactReserve
}