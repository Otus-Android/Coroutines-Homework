package otus.homework.coroutines

import retrofit2.http.GET

interface ImageService {
    @GET("api/images/get?format=json&type=jpg")
    suspend fun getCatImg() : List<Image>
}