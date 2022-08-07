package otus.homework.coroutines

import otus.homework.coroutines.models.CatImage
import retrofit2.http.GET

interface ImageService {
    @GET("meow")
    suspend fun getCatImage(): CatImage
}
