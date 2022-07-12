package otus.homework.coroutines.data.network

import otus.homework.coroutines.data.entities.Image
import retrofit2.http.GET

interface CatsImageService {
	@GET("meow")
	suspend fun getImage(): Image
}