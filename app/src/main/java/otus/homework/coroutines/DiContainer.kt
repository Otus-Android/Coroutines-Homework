package otus.homework.coroutines

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DiContainer {

    val serviceFact: CatsFactService by lazy { retrofitCatFact.create(CatsFactService::class.java) }
    val serviceImage: CatsImageService by lazy { retrofitCatImage.create(CatsImageService::class.java) }

    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
        .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitCatFact by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CatsFactService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofitCatImage by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CatsImageService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
