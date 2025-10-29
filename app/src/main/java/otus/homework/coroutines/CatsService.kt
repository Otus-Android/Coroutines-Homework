package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact
}

interface CatsImageService {

    @GET("images/search")
    suspend fun getRandomCatImage(): List<CatImage>
}