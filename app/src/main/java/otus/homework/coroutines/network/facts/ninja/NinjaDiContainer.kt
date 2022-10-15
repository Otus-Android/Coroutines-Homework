package otus.homework.coroutines.network.facts.ninja

import otus.homework.coroutines.network.facts.abs.AbsDiContainer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NinjaDiContainer: AbsDiContainer() {

    override val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override val service: NinjaCatService by lazy { retrofit.create(NinjaCatService::class.java) }
}