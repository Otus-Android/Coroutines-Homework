package otus.homework.coroutines.network.services

import otus.homework.coroutines.network.responses.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact
}