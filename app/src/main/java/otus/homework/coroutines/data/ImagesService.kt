package otus.homework.coroutines.data

import otus.homework.coroutines.models.data.Image
import retrofit2.http.GET

/**
 * Сервис получения случайных изображений
 */
interface ImagesService {

    /** Получить список случайных изображений */
    @GET("search")
    suspend fun getCatImages(): List<Image>
}