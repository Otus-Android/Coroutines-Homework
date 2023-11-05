package otus.homework.coroutines.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.data.api.FactApi
import otus.homework.coroutines.data.api.ImageUrlApi
import otus.homework.coroutines.di.annotations.scope.ApplicationScope
import otus.homework.coroutines.presentation.utlis.Internet
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class HttpsModule {

    @[Provides ApplicationScope]
    fun client(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .callTimeout(20L, TimeUnit.SECONDS)
            .build()
    }

    @[Provides ApplicationScope]
    fun factApi(okHttpClient: OkHttpClient): FactApi {
        return Retrofit.Builder()
            .baseUrl(Internet.FACT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FactApi::class.java)
    }

    @[Provides ApplicationScope]
    fun imageUrlApi(okHttpClient: OkHttpClient): ImageUrlApi {
        return Retrofit.Builder()
            .baseUrl(Internet.IMAGE_GENERATE_BAS_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageUrlApi::class.java)
    }
}