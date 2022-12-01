package otus.homework.coroutines

import retrofit2.http.GET

interface CatsImageService {
    @GET("https://aws.random.cat/meow")
    suspend fun getCatImage(): CatImage
}