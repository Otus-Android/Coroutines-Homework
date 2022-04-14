package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.data.CatsFactsService
import otus.homework.coroutines.data.CatsPhotosService
import otus.homework.coroutines.data.CatsRepo
import otus.homework.coroutines.data.CatsRepoImpl
import otus.homework.coroutines.presentation.PresenterScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiContainer {

    val catsRepo: CatsRepo by lazy { CatsRepoImpl(catsFactsService, catsPhotosService) }
    val presenterScope by lazy { PresenterScope() }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun createRetrofitInstance(url: String) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val catsFactsRetrofit = createRetrofitInstance("https://cat-fact.herokuapp.com/facts/")
    private val catsImagesRetrofit = createRetrofitInstance("https://aws.random.cat/")

    private val catsFactsService by lazy { catsFactsRetrofit.create(CatsFactsService::class.java) }
    private val catsPhotosService by lazy { catsImagesRetrofit.create(CatsPhotosService::class.java) }
}