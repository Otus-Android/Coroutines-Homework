package otus.homework.coroutines.di

import android.content.Context
import com.squareup.picasso.Picasso
import otus.homework.coroutines.presentation.viewmodel.CatsViewModel
import otus.homework.coroutines.data.converter.CatConverter
import otus.homework.coroutines.data.FactService
import otus.homework.coroutines.data.ImagesService
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.data.CatRepositoryImpl
import otus.homework.coroutines.utils.StringProvider
import otus.homework.coroutines.utils.StringProviderImpl
import otus.homework.coroutines.utils.coroutines.Dispatcher
import otus.homework.coroutines.utils.coroutines.DispatcherImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(private val context: Context) {

    val catRepository: CatRepository by lazy(LazyThreadSafetyMode.NONE) {
        CatRepositoryImpl(provideFactService(), provideImagesService(), CatConverter())
    }

    private fun provideFactService() = provideFactRetrofit().create(FactService::class.java)

    private fun provideFactRetrofit() = Retrofit.Builder()
        .baseUrl(FACT_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun provideImagesService() = provideImageRetrofit().create(ImagesService::class.java)

    private fun provideImageRetrofit() = Retrofit.Builder()
        .baseUrl(IMAGES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val stringProvider: StringProvider by lazy(LazyThreadSafetyMode.NONE) {
        StringProviderImpl(context)
    }

    val picasso: Picasso by lazy(LazyThreadSafetyMode.NONE) { Picasso.Builder(context).build() }

    val dispatcher: Dispatcher by lazy(LazyThreadSafetyMode.NONE) { DispatcherImpl() }

    val catsViewModelFactory get() = CatsViewModel.provideFactory(catRepository, stringProvider)

    private companion object {
        const val FACT_BASE_URL = "https://catfact.ninja/"
        const val IMAGES_BASE_URL = "https://api.thecatapi.com/v1/images/"
    }
}