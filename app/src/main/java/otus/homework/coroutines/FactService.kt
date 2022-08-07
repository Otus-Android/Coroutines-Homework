package otus.homework.coroutines

import otus.homework.coroutines.models.CatFact
import retrofit2.http.GET

interface FactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): CatFact
}