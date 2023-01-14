package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Response<Fact>

    @GET
    suspend fun getImage(@Url url: String) : Response<FileCats>
}

