package otus.homework.coroutines

import retrofit2.http.GET

interface MeowService {

    @GET("meow")
    suspend fun getMeow(): Meow
}