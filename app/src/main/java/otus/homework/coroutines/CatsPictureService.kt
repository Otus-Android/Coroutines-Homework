package otus.homework.coroutines

import retrofit2.http.GET

interface CatsPictureService {
    @GET("meow")
    suspend fun get(): Picture
}

