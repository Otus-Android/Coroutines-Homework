package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CatsViewModel(private val catsService: CatsService) : ViewModel() {
    private val _resultLiveData : MutableLiveData<Result?> = MutableLiveData()
    val resultLiveData : LiveData<Result?>
        get() = _resultLiveData
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _: CoroutineContext, _: Throwable ->
        CrashMonitor.trackWarning()
    }

    fun refreshPage() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                coroutineScope {
                    val catFact = async { catsService.getCatFact() }
                    val catPicture = async { catsService.getCatPicture() }

                    _resultLiveData.value = Success(CatsViewUiData(catFact.await(), catPicture.await()))
                }

            } catch (t: Throwable) {
                when (t) {
                    is java.net.SocketTimeoutException -> {
                        _resultLiveData.value = Error("Couldn't fetch response from the server")
                        _resultLiveData.value = null
                    }
                    else -> {
                        // don't we need to filter if it was coroutine's CancellationException?
                        // otherwise we could fill up analytics with cancellations as crashes
                        CrashMonitor.trackWarning()
                        t.message?.let {
                            _resultLiveData.value = Error(it)
                            _resultLiveData.value = null
                        }
                    }
                }
            }
        }
    }

    class CatsViewModelFactory(val catsService: CatsService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService) as T
        }
    }
}
