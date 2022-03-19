package otus.homework.coroutines.service

import otus.homework.coroutines.data.CatPicture
import retrofit2.http.GET

interface CatPictureService {

    @GET("meow")
    suspend fun getCatPicture() : CatPicture
}