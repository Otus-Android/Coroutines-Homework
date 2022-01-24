package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val catsService: CatsService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            CatsViewModel(catsService) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}