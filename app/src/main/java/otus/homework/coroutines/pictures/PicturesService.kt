package otus.homework.coroutines.pictures

import retrofit2.http.GET

interface PicturesService {
    @GET("meow")
    suspend fun getPicture() : Picture
}