package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface PictureService {

    @GET("meow")
    suspend fun getCatPicture() : Response<Picture>
}