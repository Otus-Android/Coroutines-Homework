package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsServiceMeow {

    @GET("meow")
    suspend fun getCatRandomPicture(): Picture
}