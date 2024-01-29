package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val retrofitFact by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.thecatapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceFact by lazy { retrofitFact.create(CatsService::class.java) }
    val serviceImage by lazy { retrofitImage.create(ImageService::class.java) }


}