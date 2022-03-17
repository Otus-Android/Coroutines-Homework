package otus.homework.coroutines.services

import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatsFactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}