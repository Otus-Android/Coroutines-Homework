package otus.homework.coroutines.data

import otus.homework.coroutines.models.data.Image
import retrofit2.http.GET

interface ImagesService {

    @GET("search")
    suspend fun getCatImages(): List<Image>
}