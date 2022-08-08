package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Url

interface CatsImageService {

    @GET
    suspend fun getCatImage(@Url loginUrl: String) : Image
}