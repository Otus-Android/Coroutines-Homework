package otus.homework.coroutines.data.api

import otus.homework.coroutines.data.model.ImageUrlEntity
import retrofit2.http.GET

interface ImageUrlApi {

    @GET("/v1/images/search")
    suspend fun getImageUrl(): List<ImageUrlEntity>
}