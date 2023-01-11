package otus.homework.coroutines.api_services

import otus.homework.coroutines.data.CatImage
import retrofit2.http.GET

interface ImageCatsService {
    @GET("meow")
    suspend fun getCatImage() : CatImage
}