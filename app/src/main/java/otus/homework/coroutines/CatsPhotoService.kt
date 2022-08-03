package otus.homework.coroutines

import retrofit2.http.GET

interface CatsPhotoService {
    @GET("meow")
    suspend fun getCatPhoto(): CatPhoto
}