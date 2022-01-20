package otus.homework.coroutines.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer

class CatsViewModelFactory(private val di: DiContainer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == CatsViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return CatsViewModel(di.serviceFact, di.servicePic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}