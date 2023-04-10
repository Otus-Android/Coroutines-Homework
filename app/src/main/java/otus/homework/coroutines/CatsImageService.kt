package otus.homework.coroutines

import retrofit2.http.GET

interface CatsImageService {

    @GET("images/search")
    suspend fun getCatImage() : List<Image>
}