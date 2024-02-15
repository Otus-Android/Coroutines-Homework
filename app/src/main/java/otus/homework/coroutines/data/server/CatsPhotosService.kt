package otus.homework.coroutines.data.server

import otus.homework.coroutines.data.server.dto.PhotoDto
import retrofit2.http.GET

interface CatsPhotosService {

    @GET("search")
    suspend fun getCatPhoto(): List<PhotoDto>
}