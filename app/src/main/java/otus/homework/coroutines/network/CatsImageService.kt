package otus.homework.coroutines.network

import otus.homework.coroutines.network.models.CatsImage
import retrofit2.http.GET

interface CatsImageService {
    @GET("meow")
    suspend fun getCatImageUrl() : CatsImage
}