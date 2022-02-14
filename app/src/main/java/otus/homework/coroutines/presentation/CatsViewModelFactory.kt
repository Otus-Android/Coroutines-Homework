package otus.homework.coroutines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.services.CatImageService
import otus.homework.coroutines.services.CatsService

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: CatImageService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsService, imageService) as T
        } else {
            throw IllegalArgumentException("Failed to create $modelClass")
        }
    }
}