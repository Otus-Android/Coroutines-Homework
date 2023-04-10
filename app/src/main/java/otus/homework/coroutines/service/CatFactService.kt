package otus.homework.coroutines.service

import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatFactService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}