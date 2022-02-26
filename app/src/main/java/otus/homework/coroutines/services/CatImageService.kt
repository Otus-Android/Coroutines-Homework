package otus.homework.coroutines.services

import otus.homework.coroutines.model.CatImg
import retrofit2.http.GET

interface CatImageService {

    @GET("meow")
    suspend fun getCatImage(): CatImg
}