package otus.homework.coroutines.network.facts.old

import otus.homework.coroutines.network.facts.base.AbsDiContainer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer: AbsDiContainer() {

    override val retrofitFact: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override val factService: CatsService by lazy { retrofitFact.create(CatsService::class.java) }
}