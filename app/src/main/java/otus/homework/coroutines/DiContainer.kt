package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.services.CatImageService
import otus.homework.coroutines.services.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val okHttp by lazy {
        val okHttpClient = OkHttpClient
            .Builder()

        if (BuildConfig.DEBUG) {
            okHttpClient
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
        }
        okHttpClient.build()
    }

    private val images by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    private val facts by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    val service: CatsService by lazy { facts.create(CatsService::class.java) }
    val imageService: CatImageService by lazy { images.create(CatImageService::class.java) }
}