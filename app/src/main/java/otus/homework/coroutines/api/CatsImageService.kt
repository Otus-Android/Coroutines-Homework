package otus.homework.coroutines.api

import retrofit2.http.GET

interface CatsImageService {

    @GET("v1/images/search")
    suspend fun getImage(): List<CatImage>
}