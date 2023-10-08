package otus.homework.coroutines.domain

import otus.homework.coroutines.models.domain.CatFact
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): CatFact
}