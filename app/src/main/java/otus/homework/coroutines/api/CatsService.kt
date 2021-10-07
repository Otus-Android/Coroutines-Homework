package otus.homework.coroutines.api

import otus.homework.coroutines.model.CatPic
import otus.homework.coroutines.model.Fact
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact

    @GET
    suspend fun getCatPicture(@Url picUrl: String = "https://aws.random.cat/meow"): CatPic
}