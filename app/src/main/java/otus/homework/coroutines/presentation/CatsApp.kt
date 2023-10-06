package otus.homework.coroutines.presentation

import android.app.Application
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.data.reposiroty.FactRepositoryImpl
import otus.homework.coroutines.data.api.CatsApi
import otus.homework.coroutines.data.api.ImageUrlGenerateApi
import otus.homework.coroutines.data.mapper.FactMapper
import otus.homework.coroutines.data.mapper.ImageUrlMapper
import otus.homework.coroutines.data.reposiroty.ImageUrlRepositoryImpl
import otus.homework.coroutines.di.ApplicationComponent
import otus.homework.coroutines.domain.repository.ImageUrlRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CatsApp: Application(), ApplicationComponent {

    private val httpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val okHttpClient
    get() = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .callTimeout(20L, TimeUnit.SECONDS)
        .build()

    override val factApi: CatsApi by lazy {
        Retrofit.Builder()
            .baseUrl(FACT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatsApi::class.java)
    }

    override val imageUrlGenerateApi: ImageUrlGenerateApi by lazy {
        Retrofit.Builder()
            .baseUrl(IMAGE_GENERATE_BAS_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageUrlGenerateApi::class.java)
    }

    override val factRepository: FactRepository by lazy {
        FactRepositoryImpl(
            api = factApi,
            mapper = FactMapper(),
        )
    }

    override val imageGenerateRepository: ImageUrlRepository by lazy {
        ImageUrlRepositoryImpl(
            api = imageUrlGenerateApi,
            mapper = ImageUrlMapper(),
        )
    }

    override val picasso: Picasso by lazy {
        Picasso.Builder(applicationContext).build()
    }

    companion object {
        const val FACT_BASE_URL = "https://catfact.ninja/"
        const val IMAGE_GENERATE_BAS_URL = "https://api.thecatapi.com/"
    }
}