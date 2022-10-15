package otus.homework.coroutines.network.facts.ninja

import otus.homework.coroutines.network.facts.base.AbsCatService
import retrofit2.http.GET

interface NinjaCatService : AbsCatService {

    @GET("fact")
    override suspend fun getCatFact(): NinjaFact
}