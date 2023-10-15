package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
   suspend fun getCatFact(): Fact
}
interface CatsImage {
    @GET("meow")
    suspend fun getCatImage(): CatImage
}