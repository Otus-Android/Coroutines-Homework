package otus.homework.coroutines.api

import otus.homework.coroutines.models.CatImage
import retrofit2.Response
import retrofit2.http.GET

interface CatsImageService {

    @GET("images/search?limit=1")
    suspend fun getCatImage(): Response<List<CatImage>>
}