package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Response<Fact>
}