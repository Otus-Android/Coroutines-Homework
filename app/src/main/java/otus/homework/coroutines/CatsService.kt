package otus.homework.coroutines

import android.media.Image
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact

}