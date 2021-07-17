package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getCatRandomImage() : RandomImage
}