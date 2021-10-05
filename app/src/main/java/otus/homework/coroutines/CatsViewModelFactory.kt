package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageCatsService

class CatsViewModelFactory (
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsService,imageCatsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}