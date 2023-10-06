package otus.homework.coroutines.data.services

import otus.homework.coroutines.data.model.Picture
import retrofit2.http.GET
import retrofit2.http.Url

interface PicturesService {
    @GET
    suspend fun getRandomPicture(@Url url: String): Picture
}