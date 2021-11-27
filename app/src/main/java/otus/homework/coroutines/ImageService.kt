package otus.homework.coroutines

import otus.homework.coroutines.model.Image
import retrofit2.http.GET

interface ImageService {

    @GET("meow")
    suspend fun getCatImage() : Image
}