package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CatsViewModel(
    private val catsService: CatsService,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableLiveData<Result<CatsModel>>()
    val state: LiveData<Result<CatsModel>> = _state

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception)
        _state.value = Result.Error(exception)
    }

    fun fetchCatsModel() = viewModelScope.launch(exceptionHandler) {
        _state.value = getCatFactWithImage()
    }

    private suspend fun getCatFactWithImage() = coroutineScope {
        val fact = async { getCatFact() }
        val img = async { getCatRandomImage() }
        val catsModel = CatsModel(fact.await().fact, img.await().file)
        return@coroutineScope Result.Success(catsModel)
    }

    private suspend fun getCatFact() = withContext(dispatcher) {
        return@withContext catsService.getCatFact()
    }

    private suspend fun getCatRandomImage() = withContext(dispatcher) {
        return@withContext catsService.getCatRandomImage()
    }

}