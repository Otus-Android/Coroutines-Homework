package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}

interface RandomCatService {
    @GET("meow")
    suspend fun getRandomCatImage() : CatImage
}