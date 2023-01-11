package otus.homework.coroutines.service

import otus.homework.coroutines.model.Image
import retrofit2.http.GET

interface ImageService {

  @GET("meow")
  suspend fun getCatImage(): Image
}