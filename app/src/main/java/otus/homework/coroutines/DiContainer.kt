package otus.homework.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val factRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val imageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factService by lazy { factRetrofit.create(CatsService::class.java) }

    val imageService by lazy { imageRetrofit.create(ImageService::class.java) }

    val mainDispatcher = Dispatchers.Main
    val ioDispatcher = Dispatchers.IO
}