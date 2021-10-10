package otus.homework.coroutines.presentation.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.domain.CatRandomFact
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.utils.CoroutineDispatchers
import otus.homework.coroutines.utils.CrashMonitor

class CatsViewModel(
    private val catsRepository: CatsRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {
    val catFact: LiveData<CatRandomFact>
        get() = _catFact
    private val _catFact = MutableLiveData<CatRandomFact>()
    val error: LiveData<String>
        get() = _error
    private val _error = SingleLiveEvent<String>()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> CrashMonitor.trackWarning(throwable.localizedMessage.orEmpty()) }

    fun loadCatRandomFact() {
        viewModelScope.launch(exceptionHandler) {
            val result = withContext(coroutineDispatchers.ioDispatcher) {
                catsRepository.getCatRandomFact()
            }
            when (result) {
                is Result.Success -> _catFact.value = result.value
                is Result.Error -> _error.value = result.message
            }
        }
    }
}