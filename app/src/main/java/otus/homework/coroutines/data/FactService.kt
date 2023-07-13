package otus.homework.coroutines.data

import otus.homework.coroutines.models.Fact
import retrofit2.http.GET

interface FactService {

    @GET("fact")
    suspend fun getCatFact(): Fact
}