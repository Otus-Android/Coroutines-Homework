package otus.homework.coroutines

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(private val context: Context) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    val presenterScope by lazy { PresenterScope() }

    val managerResources by lazy { ManagerResources.Base(context) }

    val errorDisplay by lazy { ErrorDisplay.Base(context) }
}