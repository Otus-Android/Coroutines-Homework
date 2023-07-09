package otus.homework.coroutines.model.network

import otus.homework.coroutines.model.network.dto.ImageDto
import retrofit2.http.GET

interface CatsApiService {

    @GET("images/search")
    suspend fun imageSearch(): List<ImageDto>
}