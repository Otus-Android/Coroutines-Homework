package otus.homework.coroutines

import retrofit2.http.GET

interface ImageService {

    @GET("v1/image?format=json")
    suspend fun getImage() : Image

}