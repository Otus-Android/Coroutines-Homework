package otus.homework.coroutines


import retrofit2.http.GET
import retrofit2.http.Url
import com.google.gson.annotations.SerializedName


interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
    @GET
    suspend fun getCatimg(@Url url:String) : ImageFact
}