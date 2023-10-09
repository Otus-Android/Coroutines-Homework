package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory(
    private val catFactService: CatsService,
    private val randomCatService: RandomCatService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catFactService, randomCatService) as T
    }

}