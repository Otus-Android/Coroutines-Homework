package otus.homework.coroutines.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.presentation.CatsViewModel

object CatsViewModelFactory {

    fun get() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(DiContainer.service) as T
        }
    }

}