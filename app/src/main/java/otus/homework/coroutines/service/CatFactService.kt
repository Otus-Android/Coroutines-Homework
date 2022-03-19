package otus.homework.coroutines.service

import otus.homework.coroutines.data.CatFact
import retrofit2.http.GET

interface CatFactService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : CatFact
}