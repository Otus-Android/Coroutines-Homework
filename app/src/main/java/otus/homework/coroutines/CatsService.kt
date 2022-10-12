package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {
    /** Unfortunately the server isn't worked */
    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getRandomImage(): ImageDescription

    @GET("https://catfact.ninja/fact")
    suspend fun getCatFactReserve(): FactReserve
}