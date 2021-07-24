package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_IN_SEC = 20L

class DiContainer {

    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        .build()

    private val factRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://dog-facts-api.herokuapp.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val pictureRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val factsService: CatsService by lazy { factRetrofit.create(CatsService::class.java) }
    val pictureService: PictureService by lazy { pictureRetrofit.create(PictureService::class.java) }

    val presenterScope = CoroutineScope(Dispatchers.Default + CoroutineName("CatsCoroutine") + SupervisorJob())
}