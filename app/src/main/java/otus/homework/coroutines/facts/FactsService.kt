package otus.homework.coroutines.facts

import retrofit2.http.GET

interface FactsService {
    @GET("random?animal_type=cat")
    suspend fun getFact(): Fact
}