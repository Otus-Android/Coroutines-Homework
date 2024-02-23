package otus.homework.coroutines

import android.media.Image
import retrofit2.Response
import retrofit2.http.GET

interface CatsImage {
    @GET("images/search")
    suspend fun getCatImage() : Response<List<CatImage>>
}