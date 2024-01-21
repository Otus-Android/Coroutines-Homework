package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imagesService) as T
}