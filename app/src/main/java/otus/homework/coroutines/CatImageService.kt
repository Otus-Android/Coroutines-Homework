package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Query

interface CatImageService {

    @GET("cat")
    suspend fun getCatImage(@Query("json") json: Boolean = true): CatDetails
}
