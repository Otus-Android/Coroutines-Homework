package otus.homework.coroutines

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val okHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor { s ->
            Log.i("HttpLog", s)
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private val catsRetrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val imgRetrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val catsService by lazy { catsRetrofit.create(CatsService::class.java) }

    val imgService by lazy { imgRetrofit.create(ImgService::class.java) }
}