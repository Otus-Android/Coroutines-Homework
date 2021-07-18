package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_IN_SEC = 2L

class DiContainer {

    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://dog-facts-api.herokuapp.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    val presenterScope = CoroutineScope(Dispatchers.Default + CoroutineName("CatsCoroutine"))
}