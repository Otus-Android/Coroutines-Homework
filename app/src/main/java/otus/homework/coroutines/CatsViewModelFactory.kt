package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsPhotoService

class CatsViewModelFactory(
    private val catsFactService: CatsFactService,
    private val catsPhotoService: CatsPhotoService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsFactService, catsPhotoService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}