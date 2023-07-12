package otus.homework.coroutines.model.network

import otus.homework.coroutines.model.network.dto.FactDto
import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact(): FactDto
}
