package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }
}