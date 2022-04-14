package otus.homework.coroutines.data

import retrofit2.http.GET

interface CatsFactsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Fact
}