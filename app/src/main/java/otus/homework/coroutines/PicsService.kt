package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface PicsService {

    @GET("images/search")
    suspend fun getPicture() : Response<List<Picture>>

}