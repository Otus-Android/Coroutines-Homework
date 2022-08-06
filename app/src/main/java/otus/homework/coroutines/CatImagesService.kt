package otus.homework.coroutines

import otus.homework.coroutines.dto.Image
import retrofit2.http.GET

interface CatImagesService {

  @GET("meow")
  suspend fun getCatImageUri(): Image
}