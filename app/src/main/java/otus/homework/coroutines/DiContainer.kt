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

    private val catImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy {
        object : CatsService {
            private val catFactService = catFactRetrofit.create(CatsService::class.java)
            private val catImageService = catImageRetrofit.create(CatsService::class.java)

            override suspend fun getCatFact(): Fact {
                return catFactService.getCatFact()
            }

            override suspend fun getCatImage(): List<CatImage> {
                return catImageService.getCatImage()
            }
        }
    }
}