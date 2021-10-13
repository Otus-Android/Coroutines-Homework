package otus.homework.coroutines.services

import otus.homework.coroutines.models.ImageFact
import retrofit2.Response
import retrofit2.http.GET

interface ImageService {
    @GET("meow")
    suspend fun getCatImage() : Response<ImageFact> //
}