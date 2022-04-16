package otus.homework.coroutines

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiContainer {

    private val client = OkHttpClient.Builder().addInterceptor(MockInterceptor()).build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    val scope by lazy(mode = LazyThreadSafetyMode.NONE) {
        PresenterScope()
    }
}