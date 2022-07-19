package otus.homework.coroutines.network

import retrofit2.http.GET

interface CatImageService {

    @GET("meow")
    suspend fun getCatImage() : CatImage

}