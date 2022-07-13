package otus.homework.coroutines.api

import otus.homework.coroutines.data.Photo
import retrofit2.http.GET

interface CatsPhotoService {

    @GET("meow")
    suspend fun getCatPhoto(): Photo
}