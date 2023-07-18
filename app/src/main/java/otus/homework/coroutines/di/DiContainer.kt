package otus.homework.coroutines.di

import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.data.ImageService
import otus.homework.coroutines.data.RepositoryImpl
import otus.homework.coroutines.domain.GetCatModelUseCase
import otus.homework.coroutines.presentation.mvvm.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val factRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val factService by lazy { factRetrofit.create(CatsService::class.java) }

    private val imageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val imageService by lazy { imageRetrofit.create(ImageService::class.java) }

    private val repository by lazy { RepositoryImpl(factService, imageService) }

    val useCase by lazy { GetCatModelUseCase(repository) }

    val mainViewModel by lazy { MainViewModel(useCase) }
}
