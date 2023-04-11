package otus.homework.coroutines

import retrofit2.http.GET

interface PicService {
    @GET("search")
    suspend fun getCatPic():ArrayList<CatPic>
}