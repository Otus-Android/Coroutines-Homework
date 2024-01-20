package otus.homework.coroutines

import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET

interface CatsServicePicture {
    @GET("search")
    suspend fun getCatPictureUrl() : List<UrlPicture>
}