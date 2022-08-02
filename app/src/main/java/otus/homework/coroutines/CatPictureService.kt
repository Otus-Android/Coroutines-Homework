package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatPictureService {
    @GET("meow")
    suspend fun getCatPicture() : Picture

}