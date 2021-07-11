package otus.homework.coroutines

import retrofit2.http.GET

/**
 * @author Юрий Польщиков on 11.07.2021
 */
interface CatsImageService {

    @GET("meow")
    suspend fun getCatImage(): FactImage
}