package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _data = MutableLiveData<Result>()
    val data: LiveData<Result>
        get() = _data

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message)
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            coroutineScope {
                try {
                    val fact = async { catsService.getCatFact() }
                    //throw SocketTimeoutException()
                    val image = async { catsService.getCatImage() }
                    val cat = Result.Success(Cat(fact.await(), image.await()))
                    _data.postValue(cat)
                } catch (e: SocketTimeoutException) {
                    _data.postValue(Result.Error("Не удалось получить ответ от сервера"))
                }
            }
        }
    }
}

class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService) as T
    }
}