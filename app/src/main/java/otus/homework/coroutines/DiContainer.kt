package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            //.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofit2 by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/images/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val serviceFact by lazy { retrofit.create(CatsService::class.java) }
    val serviceImages by lazy { retrofit2.create(ImagesService::class.java) }
}