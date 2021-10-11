package otus.homework.coroutines

import retrofit2.http.GET

interface ImageService {

    @GET("meow")
    suspend fun getRandomImage(): RandomImage
}