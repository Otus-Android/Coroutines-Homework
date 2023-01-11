package otus.homework.coroutines.retrofit

import otus.homework.coroutines.Picture
import retrofit2.http.GET


interface PicturesService {

    @GET("meow")
    suspend fun getRandomPicture() : Picture

}