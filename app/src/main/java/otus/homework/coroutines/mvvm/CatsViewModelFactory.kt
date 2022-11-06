package otus.homework.coroutines.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.data.CatsService

class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == CatsViewModel::class.java) {
            CatsViewModel(catsService) as T
        } else throw IllegalAccessException("Can not create viewModel")
    }
}