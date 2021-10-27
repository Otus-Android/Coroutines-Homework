package otus.homework.coroutines

import retrofit2.http.GET

interface RandomImageService {

    @GET("meow")
    suspend fun getRandomImage() : RandomImage
}