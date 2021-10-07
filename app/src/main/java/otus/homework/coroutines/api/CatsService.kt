package otus.homework.coroutines.api

import otus.homework.coroutines.model.CatPic
import otus.homework.coroutines.model.Fact
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET
    suspend fun getCatFact(@Url baseUrl: String = "https://catfact.ninja/fact?max_length=140"): Fact

    @GET
    suspend fun getCatPicture(@Url picUrl: String = "https://aws.random.cat/meow"): CatPic
}