package otus.homework.coroutines.data.network

import otus.homework.coroutines.data.entities.Fact
import retrofit2.http.GET

interface CatsFactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}