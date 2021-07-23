package otus.homework.coroutines

import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET("meow")
    suspend fun getCatImage() : CatImage
}