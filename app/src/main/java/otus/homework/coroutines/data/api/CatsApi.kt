package otus.homework.coroutines.data.api

import otus.homework.coroutines.data.model.FactEntity
import retrofit2.http.GET

interface CatsApi {

    @GET("/fact")
    suspend fun getCatFact(): FactEntity
}