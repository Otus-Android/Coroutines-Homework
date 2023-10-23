package otus.homework.coroutines

import otus.homework.coroutines.models.Fact
import otus.homework.coroutines.models.ResponseImage
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
   suspend fun getCatFact(): Fact
}
interface CatsImage {
    @GET("search")
    suspend fun getCatImage(): ResponseImage
}