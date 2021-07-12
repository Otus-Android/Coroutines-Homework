package otus.homework.coroutines

import otus.homework.coroutines.network.AnimalImage
import otus.homework.coroutines.network.Fact
import retrofit2.Response
import retrofit2.http.GET

interface ImagesService {

    @GET("meow")
    suspend fun getRandomImage() : Response<AnimalImage>
}