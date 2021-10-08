package otus.homework.coroutines

import retrofit2.http.GET

interface CatsServicePicture {

    @GET("meow")
    suspend fun getCatPicture() : CatPictureUrl
}