package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DiContainer {

    private val factRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService: CatsService by lazy { factRetrofit.create(CatsService::class.java) }

    private val imageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://random.imagecdn.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val imageService: ImageService by lazy { imageRetrofit.create(ImageService::class.java) }

}