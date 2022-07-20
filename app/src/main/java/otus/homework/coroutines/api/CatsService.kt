package otus.homework.coroutines.api

import otus.homework.coroutines.models.CatFact
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): CatFact
}
