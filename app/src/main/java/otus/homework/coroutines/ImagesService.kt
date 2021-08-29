package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImagesService {
    @GET("meow")
    suspend fun getCatImg(): Response<Img>
}