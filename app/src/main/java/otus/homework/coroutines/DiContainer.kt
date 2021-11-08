package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val factService by lazy { getRetrofit("https://cat-fact.herokuapp.com/facts/").create(CatsService::class.java) }
    val imageService by lazy { getRetrofit("https://aws.random.cat/").create(ImageService::class.java) }

}