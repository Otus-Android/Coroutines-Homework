package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val factService: CatsService by lazy { configureRetrofit("https://cat-fact.herokuapp.com/facts/").create(CatsService::class.java) }

    val randomImageService: RandomImageService by lazy { configureRetrofit("https://aws.random.cat/").create(RandomImageService::class.java) }

    private fun configureRetrofit(baseUrl: String) =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}