package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(DiContainer().service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}