package otus.homework.coroutines

import retrofit2.http.GET

interface CatImageService {

    @GET("meow")
    suspend fun getCatImage() : ArrayList<CatImage>
}