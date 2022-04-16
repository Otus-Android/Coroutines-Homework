package otus.homework.coroutines.network.services

import otus.homework.coroutines.network.dto.CatImage
import retrofit2.http.GET

interface ImageService {

    @GET("meow")
    suspend fun getCatImage() : CatImage
}