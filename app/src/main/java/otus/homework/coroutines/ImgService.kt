package otus.homework.coroutines

import retrofit2.http.GET

interface ImgService {

    @GET("breeds/image/random")
    suspend fun getRandomImg() : ImgUrl
}
