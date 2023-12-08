package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImageService {

    @GET("search")
    suspend fun getImage(): Response<List<Image>>
}
