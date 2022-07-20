package otus.homework.coroutines.api

import otus.homework.coroutines.models.CatImage
import retrofit2.http.GET

interface ImagesService {

    @GET("meow")
    suspend fun getCatImage(): CatImage
}
