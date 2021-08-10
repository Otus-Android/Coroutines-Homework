package otus.homework.coroutines.service

import otus.homework.coroutines.model.Fact
import retrofit2.Call
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}