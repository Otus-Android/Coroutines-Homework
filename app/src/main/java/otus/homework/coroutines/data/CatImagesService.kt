package otus.homework.coroutines.data

import otus.homework.coroutines.models.Image
import otus.homework.coroutines.models.Result
import retrofit2.http.GET

interface CatImagesService {
    @GET("search")
    suspend fun getCatImage(): Array<Image>
}