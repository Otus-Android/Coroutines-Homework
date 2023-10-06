package otus.homework.coroutines

import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsService {

    @GET("fact")
    fun getCatFact() : Call<Fact>

    @GET("fact")
    suspend fun getCatFactWithCoroutines() : Fact

    @GET("api/")
    suspend fun getRandomPicture(@Query("q") q: String, @Query("key") key: String = "15813887-db28635529fd4ce8ef9aa7dbd", @Query("image_type") imageType: String= "photo") : Picture
}