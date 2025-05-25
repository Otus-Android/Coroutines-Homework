package otus.homework.coroutines

import retrofit2.http.GET

interface PhotoService {

    @GET("search")
    suspend fun getPhoto(): List<Photo>
}