package otus.homework.coroutines.di

import kotlinx.coroutines.Dispatchers
import otus.homework.coroutines.retrofit.CatsImageService
import otus.homework.coroutines.retrofit.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_FACTS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }

    private val retrofitCatsImage by lazy {
        Retrofit.Builder()
            .baseUrl(URL_IMAGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceCatsImage: CatsImageService by lazy { retrofitCatsImage.create(CatsImageService::class.java) }

    val dispatcherIO by lazy { Dispatchers.IO }

    companion object {
        private const val URL_FACTS = "https://cat-fact.herokuapp.com/facts/"
        private const val URL_IMAGE = "https://aws.random.cat/"
    }
}