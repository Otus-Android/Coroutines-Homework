package otus.homework.coroutines.data

import retrofit2.http.GET

import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.ImgResource

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact

    @GET("https://aws.random.cat/meow")
    suspend fun getImageResource() : ImgResource
}