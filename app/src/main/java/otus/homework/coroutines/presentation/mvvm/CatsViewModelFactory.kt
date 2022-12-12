package otus.homework.coroutines.presentation.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer

class CatsViewModelFactory(private val diContainer: DiContainer) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(
                catsFactService = diContainer.serviceFact,
                catsImgService = diContainer.serviceImg,
                errorDisplay = diContainer.errorDisplay,
                managerResources = diContainer.managerResources
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
