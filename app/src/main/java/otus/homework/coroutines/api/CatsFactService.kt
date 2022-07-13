package otus.homework.coroutines.api

import otus.homework.coroutines.data.Fact
import retrofit2.http.GET

interface CatsFactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact
}