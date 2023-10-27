package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.catsfeature.CatsFactService
import otus.homework.coroutines.catsfeature.CatsImageService
import otus.homework.coroutines.catsfeature.GetCatInfoUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(
    private val debug: Boolean = false
) {
    private val okHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (debug)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private val catsFactRetrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val catsImageRetrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsFactService by lazy { catsFactRetrofit.create(CatsFactService::class.java) }

    val catsImageService by lazy { catsImageRetrofit.create(CatsImageService::class.java) }

    fun getCrashMonitor(tag: String = "MyApp"): CrashMonitor = if (debug) CrashMonitorImpl(tag) else CrashMonitorEmpty()

    fun getCatcInfoUseCase() = GetCatInfoUseCase(
        catsFactService = catsFactService,
        catsImageService = catsImageService,
    )
}