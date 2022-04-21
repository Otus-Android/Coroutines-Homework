package otus.homework.coroutines.api

import otus.homework.coroutines.models.CatPhoto
import retrofit2.http.GET

interface CatPhotoService {
    @GET("meow")
    suspend fun getCatPhoto(): CatPhoto
}