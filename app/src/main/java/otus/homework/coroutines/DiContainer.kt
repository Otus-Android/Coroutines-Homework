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

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }
}

class PicturesContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomfox.ca/floof/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: PicturesService by lazy { retrofit.create(PicturesService::class.java) }
}
