package otus.homework.coroutines

import otus.homework.coroutines.model.Image
import retrofit2.http.GET

interface ImagesService {

    @GET("v1/images/search")
    suspend fun getImage(): List<Image>
}