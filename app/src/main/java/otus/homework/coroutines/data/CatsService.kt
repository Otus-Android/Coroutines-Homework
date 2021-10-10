package otus.homework.coroutines.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET
    suspend fun getCatFact(@Url url: String, ): Response<Fact>

    @GET
    suspend fun getCatImage(@Url url: String): Response<Image>
}