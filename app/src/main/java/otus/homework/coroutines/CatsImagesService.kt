package otus.homework.coroutines

import retrofit2.http.GET

interface CatsImagesService {

    /**
     * Возвращает 10 url картинок по-умолчанию (ограничение бесплатного использования api)
     */
    @GET("v1/images/search")
    suspend fun getCatImages(): List<CatImage>
}