package otus.homework.coroutines.retrofit

import otus.homework.coroutines.model.CatImage
import retrofit2.http.GET

interface CatsImageService {

    @GET("meow")
    suspend fun getCatImage() : CatImage?
}