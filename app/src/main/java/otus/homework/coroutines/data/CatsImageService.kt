package otus.homework.coroutines.data

import otus.homework.coroutines.data.dto.ImageDto
import retrofit2.http.GET

interface CatsImageService {
    @GET("meow")
    suspend fun getCatImage(): ImageDto
}