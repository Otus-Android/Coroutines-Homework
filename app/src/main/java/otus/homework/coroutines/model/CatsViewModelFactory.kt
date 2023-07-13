package otus.homework.coroutines.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.CatViewModel
import otus.homework.coroutines.CatsFactService
import otus.homework.coroutines.CatsImageService

class CatsViewModelFactory(private val factService: CatsFactService,
                           private val imageService: CatsImageService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatViewModel(factService, imageService) as T
    }
}
