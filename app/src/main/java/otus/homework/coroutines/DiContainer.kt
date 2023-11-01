package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }
    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    private val retrofitForFacts by lazy {
        retrofitBuilder
            .baseUrl("https://catfact.ninja/")
            .build()
    }

    private val retrofitForImages by lazy {
        retrofitBuilder
            .baseUrl("https://api.thecatapi.com")
            .build()
    }

    val service by lazy { retrofitForFacts.create(CatsFactsService::class.java) }
    val imageService by lazy { retrofitForImages.create(CatsImagesService::class.java) }
}