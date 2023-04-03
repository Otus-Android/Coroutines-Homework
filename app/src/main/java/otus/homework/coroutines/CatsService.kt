package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}

interface ImageService {

    @GET("meow")
    suspend fun getCatImage() : Image
}