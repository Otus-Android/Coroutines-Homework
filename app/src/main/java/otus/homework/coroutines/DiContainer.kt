package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catRetrofitUrl by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val catImageRetrofitUrl by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactService: CatsService by lazy { catRetrofitUrl.create(CatsService::class.java) }
    val catImageService: CatsService by lazy { catImageRetrofitUrl.create(CatsService::class.java) }
}
