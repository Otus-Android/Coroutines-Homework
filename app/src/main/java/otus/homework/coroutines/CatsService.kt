package otus.homework.coroutines

import otus.homework.coroutines.domain.CatImage
import otus.homework.coroutines.domain.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Response<Fact>

    @GET("meow")
    suspend fun getCatImage() : Response<CatImage>
}