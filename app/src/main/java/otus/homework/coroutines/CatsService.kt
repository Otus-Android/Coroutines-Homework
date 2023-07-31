package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact
}

interface PicturesService {

    @GET(".")
    suspend fun getPicture(): Fox
}
