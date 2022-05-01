package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitText by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val textService by lazy { retrofitText.create(CatsService::class.java) }
    val imageService by lazy { retrofitImage.create(CatsImageService::class.java) }
}