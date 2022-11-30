package otus.homework.coroutines.data.img

import retrofit2.http.GET

interface CatsImgService {

    @GET("meow")
    suspend fun getCatImg(): Img
}