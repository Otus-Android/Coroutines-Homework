package otus.homework.coroutines.data.services

import otus.homework.coroutines.data.model.Fact
import otus.homework.coroutines.data.model.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    fun getCatFact() : Call<Fact>

    @GET
    suspend fun getCatFactWithCoroutines(@Url url: String) : Fact

//    @GET("api/")
//    suspend fun getRandomPicture(@Query("q") q: String, @Query("key") key: String = "15813887-db28635529fd4ce8ef9aa7dbd", @Query("image_type") imageType: String= "photo") : Picture
}