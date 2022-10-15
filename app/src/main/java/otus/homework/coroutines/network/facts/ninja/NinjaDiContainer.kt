package otus.homework.coroutines.network.facts.ninja

import otus.homework.coroutines.network.facts.base.AbsDiContainer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NinjaDiContainer: AbsDiContainer() {

    override val retrofitFact: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override val factService: NinjaCatService by lazy { retrofitFact.create(NinjaCatService::class.java) }
}