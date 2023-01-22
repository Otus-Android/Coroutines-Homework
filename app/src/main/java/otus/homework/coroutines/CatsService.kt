package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact

    @GET("meow")
    suspend fun catImage(): CatImage
}
