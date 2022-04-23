package otus.homework.coroutines.viewmodel_variant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.network.CatDataRepository

@Suppress("UNCHECKED_CAST")
class CatsViewModelFactory(
    private val catDataRepository: CatDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catDataRepository) as T
    }
}