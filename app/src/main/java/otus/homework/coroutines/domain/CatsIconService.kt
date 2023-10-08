package otus.homework.coroutines.domain

import otus.homework.coroutines.models.domain.CatIcon
import retrofit2.http.GET

interface CatsIconService {

    @GET("search")
    suspend fun getIcons(): List<CatIcon>
}