package otus.homework.coroutines.catsfeature

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact(): CatFact
}

data class CatFact(
    @SerializedName("fact")
    val text: String,
)

interface CatsImageService {

    @GET("images/search")
    suspend fun getCatsImages(): List<CatImage>
}

data class CatImage(
    @SerializedName("url")
    val url: String
)