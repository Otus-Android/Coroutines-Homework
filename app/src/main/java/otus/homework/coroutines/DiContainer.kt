package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    fun getCrashMonitor(tag: String = "MyApp"): CrashMonitor = if (debug) CrashMonitorImpl(tag) else CrashMonitorEmpty()

    fun getCatsPresenter() = CatsPresenter(
        catsService = service,
        crashMonitor = getCrashMonitor("CatsPresenter")
    )
}