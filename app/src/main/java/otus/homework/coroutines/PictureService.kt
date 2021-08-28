package otus.homework.coroutines

import retrofit2.http.GET

interface PictureService {

    @GET("api/breeds/image/random")
    suspend fun getCatPicture() : Picture
}