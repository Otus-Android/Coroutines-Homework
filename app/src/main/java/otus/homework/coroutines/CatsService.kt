package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Response<Fact>
}

interface CatsImageService {
    @GET("meow")
    suspend fun getCatImage(): Response<CatImage>
}
