package otus.homework.coroutines

import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.model.CatImage
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : CatFact

    @GET("https://aws.random.cat/meow")
    suspend fun getCatImage() : CatImage
}