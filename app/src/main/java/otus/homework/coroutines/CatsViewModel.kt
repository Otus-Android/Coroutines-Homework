package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _state = MutableLiveData<Result<CatModel>>()
    val state: LiveData<Result<CatModel>> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is SocketTimeoutException -> _state.value = Result.Error("Не удалось получить ответ от сервера")
            else -> {
                CrashMonitor.trackWarning()
                _state.value = throwable.message?.let { Result.Error(it) }
            }
        }
    }

    fun fetchData() {
        viewModelScope.launch(exceptionHandler) {
            val fact = catsFactService.getCatFact()
            val image = catsImageService.getCatImage().first()
            _state.postValue(Result.Success(CatModel(fact, image)))
        }
    }

    init {
        fetchData()
    }
}

class CatsViewModelFactory(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsFactService, catsImageService) as T
    }
}
