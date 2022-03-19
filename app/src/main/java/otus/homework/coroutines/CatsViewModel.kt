package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    private val catsLiveData = MutableLiveData<Result>()

    lateinit var catsServiceFact: CatsService
    lateinit var catsServiceImg: CatsService
    val exceptionHandler = CoroutineExceptionHandler { _, _, ->
        CrashMonitor.trackWarning()
    }
    val getObservableData: LiveData<Result>
        get() = catsLiveData

    fun requestData() {
        viewModelScope.launch(exceptionHandler) {
            try {
                coroutineScope {
                    var fact: Fact? = null
                    var image: Image? = null
                    val factRequest = async {
                        fact = withContext(Dispatchers.Default) {
                            catsServiceFact.getCatFact()
                        }
                    }

                    val imageRequest = async {
                        image = withContext(Dispatchers.Default) {
                            catsServiceImg.getCatImage()
                        }
                    }

                    awaitAll(factRequest, imageRequest)
                    catsLiveData.value = Result.Success(IllustratedFact(image!!, fact!!))
                }
            }
            catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        catsLiveData.value = Result.Error(null, R.string.socket_timeout_error)
                    }
                    is CancellationException -> {
                        throw(ex)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        catsLiveData.value = Result.Error(ex.message, null)
                    }
                }
            }
        }
    }
}