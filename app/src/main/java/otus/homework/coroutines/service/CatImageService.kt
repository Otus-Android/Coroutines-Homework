package otus.homework.coroutines.service

import otus.homework.coroutines.model.CatImage
import retrofit2.http.GET

interface CatImageService {

    @GET("v1/images/search")
    suspend fun getCatImage(): List<CatImage>
}