package otus.homework.coroutines.data

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): FactDto
}
