package otus.homework.coroutines

import retrofit2.http.GET

interface FactsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}

interface PicsService {

    @GET("meow")
    suspend fun getCatPicture() : Picture
}