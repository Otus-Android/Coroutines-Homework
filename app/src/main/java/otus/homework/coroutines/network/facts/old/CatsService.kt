package otus.homework.coroutines.network.facts.old

import otus.homework.coroutines.network.facts.base.AbsCatService
import retrofit2.http.GET

interface CatsService : AbsCatService {

    @GET("facts/random?animal_type=cat")
    override suspend fun getCatFact(): Fact
}