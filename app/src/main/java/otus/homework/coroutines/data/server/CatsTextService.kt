package otus.homework.coroutines.data.server

import otus.homework.coroutines.data.server.dto.FactDto
import retrofit2.http.GET

interface CatsTextService {

    @GET("fact")
    suspend fun getCatFact() : FactDto
}