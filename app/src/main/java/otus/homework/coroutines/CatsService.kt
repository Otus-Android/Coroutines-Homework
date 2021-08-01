package otus.homework.coroutines

import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("api/v1/resources/dogs?number=1")
    suspend fun getCatFact() : List<Fact>

    @GET("meow")
    suspend fun getCatImage() : CatImage
}