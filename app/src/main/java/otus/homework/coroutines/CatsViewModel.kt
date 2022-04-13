package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsPictureService: CatsPictureService
) : ViewModel() {
    private val liveDataMutable = MutableLiveData<Result<Cat>>()
    val liveData: LiveData<Result<Cat>> = liveDataMutable

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        if (throwable is SocketTimeoutException) {
            liveDataMutable.value = Result.Error("Не удалось получить ответ от сервером")
        } else {
            CrashMonitor.trackWarning()
            liveDataMutable.value = Result.Error(throwable.message ?: "")
        }
    }

    fun load() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async {
                catsService.getCatFact()
            }
            val pict = async {
                catsPictureService.get()
            }
            liveDataMutable.value = Result.Success(Cat(fact.await(), pict.await().file))
        }
    }

    sealed class Result<out T> {
        data class Success<T>(val result: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    class Factory(
        private val catsService: CatsService,
        private val catsPictureService: CatsPictureService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                return CatsViewModel(catsService, catsPictureService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}