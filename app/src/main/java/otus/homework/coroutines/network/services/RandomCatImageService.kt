package otus.homework.coroutines.network.services

import otus.homework.coroutines.network.responses.RandomCatImageResponse
import retrofit2.http.GET

interface RandomCatImageService {

    @GET("meow")
    suspend fun getRandomCatImage(): RandomCatImageResponse
}