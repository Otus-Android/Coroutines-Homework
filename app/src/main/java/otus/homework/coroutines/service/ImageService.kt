package otus.homework.coroutines.service

import otus.homework.coroutines.entity.Picture
import retrofit2.http.GET

interface ImageService {

    @GET("meow")
    suspend fun getCatImage(): Picture
}