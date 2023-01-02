package otus.homework.coroutines.data

import otus.homework.coroutines.model.CatImageUrl
import otus.homework.coroutines.model.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getFact() : Response<Fact>

    @GET("https://aws.random.cat/meow")
    suspend fun getImageUrl() : Response<CatImageUrl>
}