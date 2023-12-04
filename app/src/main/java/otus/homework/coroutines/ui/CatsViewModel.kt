package otus.homework.coroutines.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.Result
import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsImageService

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _uiState = MutableLiveData<Result>()
    val uiState: LiveData<Result> = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _uiState.value = Result.Error(throwable)
    }

    fun fetchData() = viewModelScope.launch(exceptionHandler) {
        val catUi = CatUi(
            fact = catsFactService.getCatFact().fact,
            imageUrl = catsImageService.getImage().first().url
        )
        _uiState.postValue(Result.Success(catUi))
    }

    class Factory(
        private val catsFactService: CatsFactService,
        private val catsImageService: CatsImageService
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsFactService, catsImageService) as T
        }
    }
}