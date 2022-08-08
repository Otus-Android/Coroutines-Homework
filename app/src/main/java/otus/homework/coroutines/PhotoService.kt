package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET

interface PhotoService {

    @GET("meow")
    suspend fun getPhoto() : Photo?
}