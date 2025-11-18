package otus.homework.coroutines.data

import retrofit2.http.GET

interface CatsImageService {
    @GET("v1/images/search")
    suspend fun getCatImage(): List<CatsImage>
}
