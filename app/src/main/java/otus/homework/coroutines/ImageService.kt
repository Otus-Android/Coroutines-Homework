package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET
import java.lang.Exception


fun <T> handleResponse(response: Response<T>): T{
    if (response.isSuccessful) return response.body()!!
    else throw Exception()
}

interface ImageService {
    @GET("meow")
    suspend fun getRandomImage() : Response<CatImage>
}