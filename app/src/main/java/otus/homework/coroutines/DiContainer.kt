package otus.homework.coroutines

import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.api.ImagesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(private val baseUrl: String) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsService by lazy { retrofit.create(CatsService::class.java) }
    val imageService: ImagesService by lazy { retrofit.create(ImagesService::class.java) }
}
