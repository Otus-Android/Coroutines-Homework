package otus.homework.coroutines

import retrofit2.http.GET

interface CatPicturesService {
    @GET("meow")
    suspend fun getCatPicture(): Picture
}