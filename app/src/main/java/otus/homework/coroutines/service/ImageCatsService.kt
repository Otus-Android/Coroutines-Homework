package otus.homework.coroutines.service

import otus.homework.coroutines.model.ImageCat
import retrofit2.http.GET

interface ImageCatsService {
    @GET("meow")
    suspend fun getImageCat(): ImageCat?
}