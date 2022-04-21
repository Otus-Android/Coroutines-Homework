package otus.homework.coroutines.api

import otus.homework.coroutines.models.Fact
import retrofit2.http.GET

interface CatFactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}