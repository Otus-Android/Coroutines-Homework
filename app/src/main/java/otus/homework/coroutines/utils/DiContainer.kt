package otus.homework.coroutines.utils

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import otus.homework.coroutines.data.CatsService
import java.util.concurrent.TimeUnit

class DiContainer {

    private val retrofit by lazy {
        val interceptor =  HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(MockInterceptor())
            .build()

        Retrofit.Builder()

            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val service by lazy { retrofit.create(CatsService::class.java) }
}