/**
 * Created by Ilia Shelkovenko on 23.01.2022.
 */
package otus.homework.coroutines

import retrofit2.http.GET

interface ImageService {
    @GET("meow")
    suspend fun getCatImage(): ImageRes
}