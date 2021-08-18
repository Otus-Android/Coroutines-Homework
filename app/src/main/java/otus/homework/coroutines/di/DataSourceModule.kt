package otus.homework.coroutines.di

import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import otus.homework.coroutines.feature.CatsViewModel
import otus.homework.coroutines.retrofit.CatsImageService
import otus.homework.coroutines.retrofit.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val RETRO_FACTS = "RetroFacts"
private const val RETRO_IMAGE = "RetroImage"
private const val URL_FACTS = "https://cat-fact.herokuapp.com/facts/"
private const val URL_IMAGE = "https://aws.random.cat/"

val dataSourceModule = module{

    single {
        OkHttpClient.Builder()
            .connectTimeout(600, TimeUnit.SECONDS)
            .addInterceptor (
                HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()
    }

    single<Retrofit>(named(RETRO_FACTS)) {
        Retrofit.Builder()
            .baseUrl(URL_FACTS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<CatsService> {
        get<Retrofit>(named(RETRO_FACTS)).create(CatsService::class.java)
    }

    single<Retrofit>(named(RETRO_IMAGE)) {
        Retrofit.Builder()
            .baseUrl(URL_IMAGE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<CatsImageService> {
        get<Retrofit>(named(RETRO_IMAGE)).create(CatsImageService::class.java)
    }

    single {
        Dispatchers.IO
    }

    viewModel {
        CatsViewModel(get(), get(), get())
    }
}