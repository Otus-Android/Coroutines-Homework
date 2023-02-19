package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.CatsService

class CatViewModelFactory (private val catsService: CatsService): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CatViewModel(catsService) as T
}