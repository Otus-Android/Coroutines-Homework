package otus.homework.coroutines.network.services

import otus.homework.coroutines.network.dto.CatFact
import retrofit2.http.GET

interface FactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : CatFact
}