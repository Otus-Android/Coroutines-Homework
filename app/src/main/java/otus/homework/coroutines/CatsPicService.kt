package otus.homework.coroutines

import retrofit2.http.GET

interface CatsPicService {

    @GET("meow")
    suspend fun getCatPictureUrl(): CatsPicture
}