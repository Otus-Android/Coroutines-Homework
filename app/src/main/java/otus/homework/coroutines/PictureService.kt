package otus.homework.coroutines

import retrofit2.http.GET

interface PictureService {

    @GET("meow")
    suspend fun getCatPicture() : Picture
}