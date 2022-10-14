package otus.homework.coroutines

import otus.homework.coroutines.entities.api.CatPhoto
import otus.homework.coroutines.entities.api.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getPhotoCat(): CatPhoto
}