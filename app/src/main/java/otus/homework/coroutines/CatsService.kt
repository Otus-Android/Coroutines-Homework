package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("meow")
    suspend fun getCatImage(): CatsImage

    @GET("fact")
    suspend fun getCatFact(): Fact

}


