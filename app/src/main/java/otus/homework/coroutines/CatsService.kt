package otus.homework.coroutines

import retrofit2.http.GET

interface CatsFactService {
    @GET("fact")
    suspend fun getCatFact(): Fact
}

interface CatsImageLinkService {
    @GET("meow")
    suspend fun getImageLink(): String
}