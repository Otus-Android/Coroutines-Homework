package otus.homework.coroutines.network.facts.base

import otus.homework.coroutines.network.facts.base.image.ImageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class AbsDiContainer {
    abstract val retrofitFact: Retrofit
    abstract val factService: AbsCatService

    private val retrofitImage: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val imageService: ImageService by lazy { retrofitImage.create(ImageService::class.java) }
}