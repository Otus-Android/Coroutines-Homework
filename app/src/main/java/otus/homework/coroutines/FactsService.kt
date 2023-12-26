package otus.homework.coroutines

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface FactsService {

    @GET("random?animal_type=cat")
    suspend fun getFact(): Fact
}