package otus.homework.coroutines.service

import otus.homework.coroutines.entity.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact
}