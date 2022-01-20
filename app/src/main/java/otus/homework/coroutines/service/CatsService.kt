package otus.homework.coroutines.service

import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.Picture
import retrofit2.http.GET

interface FactsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}

interface PicsService {

    @GET("meow")
    suspend fun getCatPicture() : Picture
}