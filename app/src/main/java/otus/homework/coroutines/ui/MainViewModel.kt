package otus.homework.coroutines.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import otus.homework.coroutines.usecase.CatInfoUseCase

class MainViewModel(private val catInfoUseCase: CatInfoUseCase): ViewModel() {

    private val liveDataForViewToObserve = MutableLiveData<CatInfoState>()
    private val liveData: LiveData<CatInfoState> = liveDataForViewToObserve
    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) })

    private fun handleError(throwable: Throwable) {
        liveDataForViewToObserve.value = CatInfoState.Error(throwable)
    }

    fun getCatInfo() {
        viewModelCoroutineScope.launch {
            val result = catInfoUseCase.getFactAndPicture()
            liveDataForViewToObserve.value = CatInfoState.Success(result)
        }
    }

    fun subscribeToLiveData() = liveData

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    class Factory(val catInfoUseCase: CatInfoUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(CatInfoUseCase::class.java).newInstance(catInfoUseCase) as T
        }

    }
}
