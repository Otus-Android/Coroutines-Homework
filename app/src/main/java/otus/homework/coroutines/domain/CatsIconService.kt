package otus.homework.coroutines.domain

import otus.homework.coroutines.models.domain.CatIcon
import retrofit2.http.GET

interface CatsIconService {

    @GET("search")
    suspend fun getIcons(): List<CatIcon>

    companion object {
        val DEFAULT_ICON = CatIcon(
            id = "id",
            url = "https://cdn2.thecatapi.com/images/9ua.jpg",
            width = 100,
            height = 100
        )
    }
}