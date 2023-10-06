package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Endpoints.FACT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    private val retrofit2 by lazy {
        Retrofit.Builder()
            .baseUrl(Endpoints.PICTURE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service2 by lazy { retrofit2.create(CatsService::class.java) }


}