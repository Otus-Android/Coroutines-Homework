package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getCatImage(): CatImage
}