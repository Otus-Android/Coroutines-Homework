package otus.homework.coroutines.ui.cats.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.data.remote.CatsService
import otus.homework.coroutines.ui.cats.CatsViewModel

class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsService) as T
        }

        throw IllegalArgumentException("ViewModel class is incorrect")
    }
}

