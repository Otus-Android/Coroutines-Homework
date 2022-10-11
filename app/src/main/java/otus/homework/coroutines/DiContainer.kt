package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {
    private val jsonFactory by lazy { GsonConverterFactory.create() }

    private fun buildRetrofit(baseUrl: String) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonFactory)
            .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: CatsService by lazy {
        buildRetrofit("https://cat-fact.herokuapp.com/facts/").create(CatsService::class.java)
    }

    val randomImageService by lazy {
        buildRetrofit("https://aws.random.cat/meow")
    }



}