package otus.homework.coroutines.network.facts.base.image

import retrofit2.http.GET

interface ImageService {

    @GET("meow")
    suspend fun getCatImageUrl(): CatImageUrlFile
}