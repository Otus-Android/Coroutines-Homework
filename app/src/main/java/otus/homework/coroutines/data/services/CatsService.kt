package otus.homework.coroutines.data.services

import otus.homework.coroutines.data.model.Fact
import otus.homework.coroutines.data.model.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CatsService {
    @GET
    suspend fun getCatFactWithCoroutines(@Url url: String) : Fact
    @GET
    suspend fun getRandomPicture(@Url url: String): Picture

}