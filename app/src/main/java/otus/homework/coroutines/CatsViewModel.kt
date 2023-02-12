package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.io.IOException
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {
    lateinit var catsService: CatsService

    private val _result: MutableLiveData<Result<*>> = MutableLiveData()
    val result: LiveData<Result<*>> = _result

    private val handler = CoroutineExceptionHandler { _, exception ->
        _result.postValue(Result.Error(null, exception.message))
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(SupervisorJob() + handler) {
            try {
                val factResponse = async {
                    catsService.getCatFact()
                }
                val picResponse = async {
                    catsService.getPic(DiContainer.picUrl)
                }

                val fact = factResponse.await()
                val pic = picResponse.await()

                if (fact.body() != null && pic.body() != null) {
                    _result.postValue(Result.Success(Pair(fact.body(), pic.body())))
                }
            } catch (e: SocketTimeoutException) {
                _result.postValue(Result.Error(null, R.string.failed_response))
            }
        }
    }

    fun onStop() {
        onCleared()
    }
}