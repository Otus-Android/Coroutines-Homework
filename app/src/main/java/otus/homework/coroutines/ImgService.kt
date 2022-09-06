package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImgService {

    @GET("meow")
    suspend fun getCatImg(): Response<Fact>
}