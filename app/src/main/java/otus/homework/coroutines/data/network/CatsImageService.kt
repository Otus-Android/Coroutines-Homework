package otus.homework.coroutines.data.network

import otus.homework.coroutines.model.CatImageModel
import retrofit2.http.GET

interface CatsImageService {
    @GET("images/search")
    suspend fun getCatImage(): List<CatImageModel>
}