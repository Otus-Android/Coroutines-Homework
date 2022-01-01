package otus.homework.coroutines

import otus.homework.coroutines.models.Fact
import otus.homework.coroutines.models.Image
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getCatImage() : Image
}