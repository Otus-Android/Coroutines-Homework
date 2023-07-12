package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory : ViewModelProvider.Factory {

    private val diContainer = DiContainer()
    private val catsService: CatsService = diContainer.catService
    private val catImageService: CatImageService = diContainer.catImageService

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, catImageService) as T
    }
}