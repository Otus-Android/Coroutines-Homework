package otus.homework.coroutines.data

import otus.homework.coroutines.data.dto.FactDto
import retrofit2.http.GET

interface CatsFactService {

    @GET("random?animal_type=cat")
    suspend fun getFact(): FactDto
}