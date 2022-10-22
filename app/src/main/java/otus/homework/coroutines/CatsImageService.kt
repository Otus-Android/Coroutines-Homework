package otus.homework.coroutines

import retrofit2.http.GET

interface CatsImageService {

    @GET("meow")
    suspend fun getCatImage(): ImageFact

}