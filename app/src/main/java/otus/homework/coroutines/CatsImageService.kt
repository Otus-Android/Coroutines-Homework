package otus.homework.coroutines

import otus.homework.coroutines.model.RandomCat
import retrofit2.Response
import retrofit2.http.GET

interface CatsImageService {

    @GET("cat?json=true")
    suspend fun getCatImage() : Response<RandomCat>

    companion object {
        const val BASE_URL = "https://cataas.com/"
    }
}
