package otus.homework.coroutines.network.facts.old

import otus.homework.coroutines.network.facts.abs.AbsDiContainer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer: AbsDiContainer() {

    override val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override val service: CatsService by lazy { retrofit.create(CatsService::class.java) }
}