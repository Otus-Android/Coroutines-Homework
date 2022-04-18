package otus.homework.coroutines.data

import retrofit2.http.GET

interface CatsPhotosService {

    @GET("meow")
    suspend fun getCatPhoto(): CatPhotoResponse
}