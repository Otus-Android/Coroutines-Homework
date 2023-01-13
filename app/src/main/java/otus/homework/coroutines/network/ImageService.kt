package otus.homework.coroutines.network

import otus.homework.coroutines.model.Image
import retrofit2.Response
import retrofit2.http.GET

interface ImageService {

    @GET("meow")
    suspend fun getCatImage(): Response<Image>
}