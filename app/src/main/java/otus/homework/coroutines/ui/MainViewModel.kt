package otus.homework.coroutines.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.usecase.CatInfoUseCase

class MainViewModel(private val catInfoUseCase: CatInfoUseCase) : ViewModel() {

    private val liveDataForViewToObserve = MutableLiveData<CatInfoState>()
    private val liveData: LiveData<CatInfoState> = liveDataForViewToObserve

    private fun handleError(throwable: Throwable) {
        liveDataForViewToObserve.value = CatInfoState.Error(throwable)
    }

    fun getCatInfo() {
        viewModelScope.launch {
            CoroutineExceptionHandler { _, throwable ->
                CrashMonitor.trackWarning()
            }
            try {
                val result = catInfoUseCase.getFactAndPicture()
                liveDataForViewToObserve.value = CatInfoState.Success(result)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun subscribeToLiveData() = liveData

    class Factory(val catInfoUseCase: CatInfoUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(CatInfoUseCase::class.java).newInstance(catInfoUseCase) as T
        }

    }
}
