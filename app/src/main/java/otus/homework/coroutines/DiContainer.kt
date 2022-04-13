package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun retrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsService by lazy { retrofit("https://cat-fact.herokuapp.com/facts/").create(CatsService::class.java) }
    val catsPictureService by lazy { retrofit("https://aws.random.cat/").create(CatsPictureService::class.java) }
}