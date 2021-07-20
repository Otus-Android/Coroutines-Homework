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
        val fact = async(dispatcher) { catsService.getCatFact() }
        val img = async(dispatcher) { catsService.getCatRandomImage() }
        val catsModel = CatsModel(fact.await().fact, img.await().file)
        _state.value = Result.Success(catsModel)
    }
}