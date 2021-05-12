package otus.homework.coroutines.api

import retrofit2.Response
import retrofit2.http.GET

interface CatsServiceFact {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact
}

interface CatsServiceImage {

    @GET("rest/")
    suspend fun getCatImage(): CatImage
}