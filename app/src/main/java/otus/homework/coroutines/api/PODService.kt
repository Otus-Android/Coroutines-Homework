package otus.homework.coroutines.api

import kotlinx.coroutines.Deferred
import otus.homework.coroutines.model.PODServerResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface PODService {

    @GET("planetary/apod")
    suspend fun getPictureOfTheDayByDate(
        @Query("api_key") apiKey: String
    ): PODServerResponseData
}
