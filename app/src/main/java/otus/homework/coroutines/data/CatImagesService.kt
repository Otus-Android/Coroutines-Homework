package otus.homework.coroutines.data

import otus.homework.coroutines.Image
import retrofit2.http.GET

interface CatImagesService {
    @GET("search")
    suspend fun getCatImage(): Array<Image>
}