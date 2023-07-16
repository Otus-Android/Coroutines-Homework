package otus.homework.coroutines.data

import otus.homework.coroutines.models.data.Fact
import retrofit2.http.GET

interface FactService {

    @GET("fact")
    suspend fun getCatFact(): Fact
}