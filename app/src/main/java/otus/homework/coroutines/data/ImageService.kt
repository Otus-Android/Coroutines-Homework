package otus.homework.coroutines.data

import retrofit2.http.GET

interface ImageService {

    @GET("images/search")
    suspend fun getImage(): ArrayList<ImageDto>
}
