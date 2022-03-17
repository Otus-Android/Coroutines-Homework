package otus.homework.coroutines.services

import otus.homework.coroutines.model.ImageSource
import retrofit2.http.GET

interface CatsImageService {

    @GET("meow")
    suspend fun getCatImageSource(): ImageSource
}