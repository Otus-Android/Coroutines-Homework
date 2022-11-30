package otus.homework.coroutines

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(private val context: Context) {

    private fun retrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val serviceImg by lazy { retrofit(URL_IMG).create(CatsImgService::class.java) }

    val serviceFact by lazy { retrofit(URL_FACT).create(CatsFactService::class.java) }

    val presenterScope by lazy { PresenterScope() }

    val managerResources by lazy { ManagerResources.Base(context) }

    val errorDisplay by lazy { ErrorDisplay.Base(context) }

    companion object {
        private const val URL_FACT = "https://catfact.ninja/"
        private const val URL_IMG = "https://aws.random.cat/"
    }
}