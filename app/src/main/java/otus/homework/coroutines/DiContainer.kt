package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catFactRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catService by lazy { catFactRetrofit.create(CatsService::class.java) }

    private val catImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cataas.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catImageService by lazy { catImageRetrofit.create(CatImageService::class.java) }
}
