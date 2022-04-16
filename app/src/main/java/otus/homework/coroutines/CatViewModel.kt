package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatViewModel(private val catsService: CatsService) : ViewModel() {

    private val stateLiveData = MutableLiveData<Result<CatUiState>>()
    val state: LiveData<Result<CatUiState>> = stateLiveData

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> stateLiveData.postValue(Result.Error("Не удалось получить ответ от сервера"))
            else -> stateLiveData.postValue(Result.Error("Упс, произошла ошибка: ${exception.message}"))
        }

        CrashMonitor.trackWarning(exception)
    }

    fun requestFact() {
        viewModelScope.launch(handler) {
            val fact = async {
                catsService.getCatFact()
            }

            val image = async {
                catsService.getCatImage()
            }

            val state = Result.Success(CatUiState(fact.await(), image.await()))

            stateLiveData.postValue(state)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CatViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatViewModel(catsService) as T
    }
}