package otus.homework.coroutines.presentation

import android.app.Application
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.data.reposiroty.FactRepositoryImpl
import otus.homework.coroutines.data.api.FactApi
import otus.homework.coroutines.data.api.ImageUrlApi
import otus.homework.coroutines.data.mapper.FactMapper
import otus.homework.coroutines.data.mapper.ImageUrlMapper
import otus.homework.coroutines.data.reposiroty.ImageUrlRepositoryImpl
import otus.homework.coroutines.di.ApplicationComponent
import otus.homework.coroutines.domain.repository.ImageUrlRepository
import otus.homework.coroutines.presentation.utlis.Internet.FACT_BASE_URL
import otus.homework.coroutines.presentation.utlis.Internet.IMAGE_GENERATE_BAS_URL
import otus.homework.coroutines.presentation.utlis.ViewModelFactory
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

    override val factApi: FactApi by lazy {
        Retrofit.Builder()
            .baseUrl(FACT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FactApi::class.java)
    }

    override val imageUrlApi: ImageUrlApi by lazy {
        Retrofit.Builder()
            .baseUrl(IMAGE_GENERATE_BAS_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageUrlApi::class.java)
    }

    override val factRepository: FactRepository by lazy {
        FactRepositoryImpl(
            api = factApi,
            mapper = FactMapper(),
        )
    }

    override val imageUrlRepository: ImageUrlRepository by lazy {
        ImageUrlRepositoryImpl(
            api = imageUrlApi,
            mapper = ImageUrlMapper(),
        )
    }

    override val picasso: Picasso by lazy {
        Picasso.Builder(applicationContext).build()
    }

    override val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(
            factRepository = factRepository,
            imageUrlRepository = imageUrlRepository,
        )
    }
}