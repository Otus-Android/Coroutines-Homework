package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.repo.Repository
import otus.homework.coroutines.repo.RepositoryImpl
import otus.homework.coroutines.viewModel.CatsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitForFacts by lazy {
        Retrofit.Builder()
            .baseUrl(FACTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val factService by lazy { retrofitForFacts.create(CatFactService::class.java) }

    private val retrofitForPics by lazy {
        Retrofit.Builder()
            .baseUrl(PICS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val picService by lazy { retrofitForPics.create(CatPicService::class.java) }

    private val repository: Repository by lazy {RepositoryImpl(factService, picService)}

    val catsViewModel: CatsViewModel get() = factory(repository).create(CatsViewModel::class.java)
    private fun factory(repository: Repository) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                CatsViewModel::class.java ->
                    CatsViewModel(repository) as T
                else -> {
                    error("Unknown viewModel class $modelClass")
                }
            }
        }
    }

    companion object {
        private const val FACTS_BASE_URL = "https://catfact.ninja/"
        private const val PICS_BASE_URL = "https://api.thecatapi.com/"
    }
}