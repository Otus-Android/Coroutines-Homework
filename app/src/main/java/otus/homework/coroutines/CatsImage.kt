package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsImage {

    @GET("fact")
    suspend fun getCatImage() : Response<Image>
}