package otus.homework.coroutines.data.fact

import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}