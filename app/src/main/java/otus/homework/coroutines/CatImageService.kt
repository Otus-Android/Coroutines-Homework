package otus.homework.coroutines

import retrofit2.http.GET

interface CatImageService {

    @GET("search")
    suspend fun getImage() : List<CatImage>
}