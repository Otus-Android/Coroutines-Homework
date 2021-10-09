package otus.homework.coroutines.data.remote

import otus.homework.coroutines.data.model.CatImage
import otus.homework.coroutines.data.model.Fact
import retrofit2.http.GET

interface CatsService {
    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getCatImage(): CatImage
}