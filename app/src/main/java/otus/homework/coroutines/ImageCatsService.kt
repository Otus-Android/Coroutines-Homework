package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImageCatsService {

    @GET("meow")
    suspend fun getCatImage(): Response<CatImage>
}