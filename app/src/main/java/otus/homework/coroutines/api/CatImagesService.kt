package otus.homework.coroutines.api

import otus.homework.coroutines.dto.Image
import retrofit2.http.GET

interface CatImagesService {

  @GET("meow")
  suspend fun getCatImageUri(): Image
}