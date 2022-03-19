package otus.homework.coroutines.presentation.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer

class CatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == CatsViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return CatsViewModel(
                catFactService = DiContainer.catFactService,
                catPictureService = DiContainer.catPictureService,
                resources = DiContainer.getResources(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}