package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsServiceImg {

    @GET("meow")
    suspend fun getCatImage() : CatImage
}