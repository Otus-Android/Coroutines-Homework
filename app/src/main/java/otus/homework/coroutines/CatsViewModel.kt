package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private val _data = MutableLiveData<Result>()
    val data: LiveData<Result>
        get() = _data

    private var jobCat: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message!!)
    }

    fun onInitComplete() {
        jobCat = viewModelScope.launch(exceptionHandler) {
            try {
                val fact = async(Dispatchers.IO) { catsService.getCatFact() }
                val image = async(Dispatchers.IO) { catsService.getCatImage() }
                val cat = Result.Success(Cat(fact.await(), image.await()))
                _data.postValue(cat)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.toasts("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        CrashMonitor.trackWarning(e.message!!)
                        _catsView?.toasts(e.message!!)
                    }
                }
            }
        }
    }

    fun detachView(){
        jobCat?.cancel()
    }
}

class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService) as T
    }
}