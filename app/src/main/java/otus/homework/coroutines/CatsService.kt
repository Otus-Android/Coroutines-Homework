package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {
    @GET("search?limit=1")
    suspend fun getCats(): List<Cat>
}