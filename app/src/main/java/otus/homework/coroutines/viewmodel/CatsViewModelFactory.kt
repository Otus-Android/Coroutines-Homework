package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.api.CatFactService
import otus.homework.coroutines.api.CatPhotoService

class CatsViewModelFactory(
    private val catFactService: CatFactService,
    private val catPhotoService: CatPhotoService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catFactService, catPhotoService) as T
        }
        throw IllegalArgumentException("Failed to create CatsViewModel")
    }
}