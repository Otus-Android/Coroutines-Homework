package otus.homework.coroutines.retrofit

import otus.homework.coroutines.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact
}