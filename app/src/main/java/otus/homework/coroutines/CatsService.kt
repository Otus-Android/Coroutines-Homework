package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsServiceFact {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Response<Fact>
}

interface CatsServiceImage {

    @GET("rest/")
    suspend fun getCatImage(): Response<CatImage>
}