package otus.homework.coroutines.network

import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

/**
 * Интерфейс для взаимодействия с API фактов и изображений о кошках.
 */
interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

    @GET("https://api.thecatapi.com/v1/images/search")
    suspend fun getCatImage(): List<CatImage>
}