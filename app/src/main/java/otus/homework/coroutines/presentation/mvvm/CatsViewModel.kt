package otus.homework.coroutines.presentation.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.CatRandomFact
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.utils.CrashMonitor

class CatsViewModel(private val catsRepository: CatsRepository) : ViewModel() {
    val catFact: LiveData<CatRandomFact>
        get() = _catFact
    val error: LiveData<String>
        get() = _error
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _catFact = MutableLiveData<CatRandomFact>()
    private val _error = SingleLiveEvent<String>()
    private val _isLoading = MutableLiveData<Boolean>()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            val message = throwable.localizedMessage.orEmpty()
            _error.value = message
            CrashMonitor.trackWarning(throwable.localizedMessage.orEmpty())
        }

    fun loadCatRandomFact() {
        viewModelScope.launch(exceptionHandler) {
            try {
                _isLoading.value = true
                when (val result = catsRepository.getCatRandomFact()) {
                    is Result.Success -> _catFact.value = result.value
                    is Result.Error -> _error.value = result.errorMessage
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}