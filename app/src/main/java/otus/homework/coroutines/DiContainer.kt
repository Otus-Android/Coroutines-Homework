package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitRandomCat by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceRandomCatFact: CatsService by lazy { retrofit.create(CatsService::class.java) }
    val serviceRandomCatImage: CatsService by lazy { retrofitRandomCat.create(CatsService::class.java) }
}
